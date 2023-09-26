package se.knzoon.leaguehelper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.knzoon.leaguehelper.model.Takeover;
import se.knzoon.leaguehelper.model.TakeoverPairView;
import se.knzoon.leaguehelper.model.TakeoverRepository;
import se.knzoon.leaguehelper.model.ZoneView;
import se.knzoon.leaguehelper.representation.MigrationResultRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MigrationService {

    private final TakeoverRepository takeoverRepository;
    private final RevisitChainExtractor revisitChainExtractor;

    private Logger logger = LoggerFactory.getLogger(MigrationService.class);

    @Autowired
    public MigrationService(TakeoverRepository takeoverRepository, RevisitChainExtractor revisitChainExtractor) {
        this.takeoverRepository = takeoverRepository;
        this.revisitChainExtractor = revisitChainExtractor;
    }

    /**
     * Ta hand om att lostTime skrevs in på den sista revisit takeovern och inte på den första takeovern som det borde vara
     * Buggen patchades i slutet av round 151
     * @param roundId
     * @return
     */
    public MigrationResultRepresentation migrateRevisitTakeovers(int roundId) {

//        Hämta distincta zone_id (19629 st)
//        Från javamigrering för varje zon:
//             Hämta alla takeovers som matchar zon
//             Dela upp det i kedjor
//             För varje kedja byt nextInfo sista mot första
        logger.info("Fetching all necessary zoneIds");
        List<ZoneView> zonesWithRevisits = takeoverRepository.findZonesHavingARevisitForRound(roundId);
        List<List<ZoneView>> partitions = PartitionUtil.partition(zonesWithRevisits, 200);
        logger.info("Migrating {} zones in {} chunks", zonesWithRevisits.size(), partitions.size());
        Integer nrofMigrated = partitions.stream().map(zones -> migrateChunkOfZones(zones, roundId)).collect(Collectors.summingInt(Integer::intValue));
        logger.info("Migrated {} revisit chains", nrofMigrated);
        return new MigrationResultRepresentation("Migrerat revisit takeovers", nrofMigrated);
    }

    public Integer migrateChunkOfZones(List<ZoneView> zones, Integer roundId) {
        Integer nrofChainsForChunk = zones.stream().map(zoneView -> migrateRevisitsForZone(zoneView, roundId)).collect(Collectors.summingInt(Integer::intValue));
        logger.info("Migrated {} chains", nrofChainsForChunk);
        return nrofChainsForChunk;
    }

    @Transactional
    public Integer migrateRevisitsForZone(ZoneView zoneView, Integer roundId) {
        List<Takeover> takeovers = takeoverRepository.findAllByRoundIdAndZoneIdOrderById(roundId, zoneView.getZoneId());
        List<List<Takeover>> chains = revisitChainExtractor.extractChains(takeovers);
        logger.info("Zone {} has {} chains", zoneView.getZoneId(), chains.size());
        chains.forEach(this::swapNextInfo);
        return chains.size();
    }

    private void swapNextInfo(List<Takeover> revisitChain) {
        Takeover firstTakeover = revisitChain.get(0);
        Takeover lastTakeover = revisitChain.get(revisitChain.size() - 1);

        if (firstTakeover.getLostTime() == null) {
            firstTakeover.setNextInfo(lastTakeover.getNextUser(), lastTakeover.getLostTime());
            lastTakeover.setNextInfo(null, null);
        } else {
            logger.info("No migration because after patch");
        }
    }

    /**
     * Ta hand om buggen som tillkom iom patchen av problemen med revisit
     * Felet uppkom alltså i slutet av round 151
     * @param roundId
     * @return
     */
    @Transactional
    public MigrationResultRepresentation migrateFirstTakeovers(int roundId) {
        logger.info("Fetching all necessary takeoverPairs");
        List<TakeoverPairView> firstTwoTakeoversForRound = takeoverRepository.findFirstTwoTakeoversForRound(roundId);
        List<List<TakeoverPairView>> partitions = PartitionUtil.partition(firstTwoTakeoversForRound, 200);
        logger.info("Migrating {} takeovers in {} chunks", firstTwoTakeoversForRound.size(), partitions.size());
        Integer nrofMigrated = partitions.stream().map(this::migrateChunkOfTakeovers).collect(Collectors.summingInt(Integer::intValue));
        logger.info("Migrated {} first takeovers", nrofMigrated);
        return new MigrationResultRepresentation("Migrerat first takeovers", nrofMigrated);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer migrateChunkOfTakeovers(List<TakeoverPairView> takeoverPairs) {
        Set<Long> takeoverIds = takeoverPairs.stream().map(p -> List.of(p.getFirstTakeoverId(), p.getSecondTakeoverId())).flatMap(Collection::stream).collect(Collectors.toSet());
        logger.info("Fetching all necessary takeovers {}", takeoverIds.size());
        Map<Long, Takeover> takeoverMap = takeoverRepository.findAllByIdIn(takeoverIds).stream().collect(Collectors.toMap(Takeover::getId, Function.identity()));
        return takeoverPairs.stream().map(p -> migrateTakeoverPair(p, takeoverMap)).collect(Collectors.summingInt(Integer::intValue));
    }

    private Integer migrateTakeoverPair(TakeoverPairView takeoverPair, Map<Long, Takeover> takeoverMap) {
        Takeover firstTakeover = takeoverMap.get(takeoverPair.getFirstTakeoverId());
        Takeover secondTakeover = takeoverMap.get(takeoverPair.getSecondTakeoverId());

        if (firstTakeover == null || secondTakeover == null) {
            logger.info("error migrating takeoverPair {}, {}", takeoverPair.getFirstTakeoverId(), takeoverPair.getSecondTakeoverId());
            return 0;
        }

//        logger.info("Setting nextInfo to user: {} lostTime: {} for takeover: {}", secondTakeover.getUser().getId(), secondTakeover.getTakeoverTime(), firstTakeover.getId());
        firstTakeover.setNextInfo(secondTakeover.getUser(), secondTakeover.getTakeoverTime());
        return 1;
    }
}
