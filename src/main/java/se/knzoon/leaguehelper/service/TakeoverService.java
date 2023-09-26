package se.knzoon.leaguehelper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.knzoon.leaguehelper.model.*;
import se.knzoon.leaguehelper.representation.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TakeoverService {

    private final TakeoverRepository takeoverRepository;
    private final UserRepository userRepository;
    private final LeagueRepository leagueRepository;

    private final List<String> colors;

    private Logger logger = LoggerFactory.getLogger(TakeoverService.class);

    @Autowired
    public TakeoverService(TakeoverRepository takeoverRepository, UserRepository userRepository, LeagueRepository leagueRepository) {
        this.takeoverRepository = takeoverRepository;
        this.userRepository = userRepository;
        this.leagueRepository = leagueRepository;

        colors = Arrays.asList("#FFA726", "#42A5F5", "#065535", "#FFC0CB", "#BADA55", "#800080", "#7FFFD4", "#FA8072", "#468499", "#FFD700");
    }


    @Transactional
    public GraphDataRepresentation getGraphDataForEachDay() {
        // This should be entirely rewritten!!!!!!!!!!
//        Set<User> users = getUsersInvolved(leagueId);
//        Map<Long, List<Takeover>> takeoversPerUser = getTakeoversForUsers(users);
//        int numberOfDaysInRoundYet = numberOfDaysInRoundYet(takeoversPerUser.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
//
//        ZonedDateTime now = Instant.now().atZone(ZoneId.of("UTC"));
//        List<GraphDatasetRepresentation> datasets = new ArrayList<>();
//        int i = 0;
//
//        for (User user : users) {
//            List<Takeover> takeovers = takeoversPerUser.get(user.getId());
//            logger.info("user {} has {} takeovers", user.getUsername(), takeovers.size());
//            List<Integer> pointsPerDay = calculatePointsPerDay(takeovers, now, numberOfDaysInRoundYet).stream().map(d -> (int) Math.round(d)).collect(Collectors.toList());
//            datasets.add(new GraphDatasetRepresentation(user.getUsername(), List.of(new GraphDatapointRepresentation("1", 0, 0)), colors.get(i++), "unknown", 0, 0));
//        }
//
//
//        List<String> labels = createLabels(numberOfDaysInRoundYet);
//        return new GraphDataRepresentation(datasets);
        return new GraphDataRepresentation(List.of());
    }

    @Transactional
    public GraphDataRepresentation getGraphDataCumulative(Long leagueId) {

        Optional<League> possibleLeague = leagueRepository.findById(leagueId);

        if (possibleLeague.isEmpty()) {
            return new GraphDataRepresentation(List.of());
        }

        League league = possibleLeague.get();

        Set<User> users = getUsersInvolved(league);

        Map<Long, List<Takeover>> takeoversPerUser = getTakeoversForUsers(users, league.getRoundId());
        int numberOfDaysInRoundYet = numberOfDaysInRoundYet(takeoversPerUser.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));

        ZonedDateTime now = Instant.now().atZone(ZoneId.of("UTC"));
        List<GraphDataset> datasets = new ArrayList<>();
        int i = 0;

        for (User user : users.stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList())) {
            GraphDataset dataset = new GraphDataset(numberOfDaysInRoundYet, user.getUsername(), colors.get(i++), "unknown");
            List<Takeover> takeovers = takeoversPerUser.get(user.getId());
            logger.info("user {} has {} takeovers", user.getUsername(), takeovers.size());
            List<Integer> pointsPerDay = calculatePointsCumulative(takeovers, now, numberOfDaysInRoundYet);
            List<Integer> pphPerDay = calculatePphPerDay(takeovers, numberOfDaysInRoundYet);
            dataset.addCummulativePoints(pointsPerDay);
            dataset.addPph(pphPerDay);

            datasets.add(dataset);
        }

        List<GraphDatasetRepresentation> datasetRepresentations = datasets.stream()
                .sorted(Comparator.comparing(GraphDataset::currentPoints).reversed())
                .map(GraphDataset::toRepresentation)
                .collect(Collectors.toList());

        return new GraphDataRepresentation(datasetRepresentations);
    }


    private Set<User> getUsersInvolved(League league) {
        return userRepository.findAllById(league.getUserIds()).stream().collect(Collectors.toSet());

//        Set<String> usernames = Arrays.asList("Trawlerman", "bythesea", "ChoccyMuffin", "Blackwall", "MammaGräddnos", "svenlutte", "northstar", "praktikus", "Sandra82", "Kungken1").stream().collect(Collectors.toSet());
//        Set<String> usernames = Arrays.asList("praktikus", "..Nemesis..").stream().collect(Collectors.toSet());
//        Set<String> usernames = Arrays.asList("..Nemesis..").stream().collect(Collectors.toSet());
//        Set<String> usernames = Arrays.asList("praktikus").stream().collect(Collectors.toSet());

//        return userRepository.findAllByUsernameIn(usernames).stream().collect(Collectors.toSet());
    }

    Map<Long, List<Takeover>> getTakeoversForUsers(Set<User> users, Integer roundId) {
        List<Takeover> allTakeovers = takeoverRepository.findAllByRoundIdAndUserInOrderByTakeoverTime(roundId, users);

        Map<Long, List<Takeover>> takeoversPerUser = allTakeovers.stream().collect(Collectors.groupingBy(t -> t.getUser().getId()));
        users.forEach(user -> takeoversPerUser.putIfAbsent(user.getId(), new ArrayList<>()));
        return takeoversPerUser;
    }

    private int numberOfDaysInRoundYet(List<Takeover> takeovers) {
        if (takeovers.isEmpty()) {
            return 1;
        }

        takeovers.sort(Comparator.comparing(Takeover::getId).reversed());
        return takeovers.get(0).dayOfRound();
    }

    private List<String> createLabels(int numberOfDaysInRoundYet) {
        List<String> labels = new ArrayList<>();

        for (int i = 1; i < numberOfDaysInRoundYet + 1; i++) {
            labels.add(Integer.valueOf(i).toString());
        }

        return labels;
    }

    private List<Integer> calculatePointsCumulative(List<Takeover> takeovers, ZonedDateTime now, int numberOfDaysInRoundYet) {
        List<Double> pointsPerDay = calculatePointsPerDay(takeovers, now, numberOfDaysInRoundYet);
        List<Integer> cumulativePoints = new ArrayList<>();

        Double partialSum = 0.0;

        for (Double dayPoints : pointsPerDay) {
            partialSum += dayPoints;
            cumulativePoints.add((int) Math.round(partialSum));
        }

//        pointsPerDay.forEach(d -> logger.info("per day {}", d));
//        cumulativePoints.forEach(i -> logger.info("per day rounded {}", i));

        return cumulativePoints;
    }



    private List<Double> calculatePointsPerDay(List<Takeover> takeovers, ZonedDateTime now, int numberOfDaysInRoundYet) {
        List<Double> pointsPerDay = new ArrayList<>();

        Map<Integer, List<Takeover>> takeoversPerDay = takeovers.stream().collect(Collectors.groupingBy(Takeover::dayOfRound));

        for (int i = 1; i < numberOfDaysInRoundYet + 1; i++) {
//            logger.info("Day {}", i);
            pointsPerDay.add(calculatePointsForDay(takeoversPerDay.get(i), now));
        }


        return pointsPerDay;
    }

    private Double calculatePointsForDay(List<Takeover> takeovers, ZonedDateTime now) {
        if (takeovers == null || takeovers.isEmpty()) {
            return 0.0;
        }

        return takeovers.stream().map(t -> t.pointsUntilNow(now)).collect(Collectors.summingDouble(Double::doubleValue));

//        int roundedSum = (int) Math.round(sum);
//
//        logger.info("Sum: {}, {}", roundedSum, sum);

    }

    private List<Integer> calculatePphPerDay(List<Takeover> takeovers, int numberOfDaysInRoundYet) {
        List<Integer> pphPerDay = new ArrayList<>();

        Map<Integer, List<Takeover>> takeoversPerDay = takeovers.stream().collect(Collectors.groupingBy(Takeover::dayOfRound));

        for (int i = 1; i < numberOfDaysInRoundYet + 1; i++)  {
            pphPerDay.add(calculatePphForDay(takeoversPerDay.get(i)));
        }

        return pphPerDay;
    }

    private Integer calculatePphForDay(List<Takeover> takeovers) {
        if (takeovers == null || takeovers.isEmpty()) {
            return 0;
        }

        return takeovers.stream().map(Takeover::getActivePph).collect(Collectors.summingInt(Integer::intValue));
    }

    @Transactional
    public List<LeagueRepresentation> searchLeagues(Integer roundId, Integer division, Integer group) {
        List<League> leagues;
        if (roundId != null) {
            if (division != null) {
                if (group != null) {
                    leagues = leagueRepository.findAllByRoundIdAndDivisionIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(roundId, division, group);
                } else {
                    leagues = leagueRepository.findAllByRoundIdAndDivisionIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(roundId, division);
                }
            } else {
                if (group != null) {
                    leagues = leagueRepository.findAllByRoundIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(roundId, group);
                } else {
                    leagues = leagueRepository.findAllByRoundIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(roundId);
                }
            }
        } else {
            if (division != null) {
                if (group != null) {
                    leagues = leagueRepository.findAllByDivisionIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(division, group);
                } else {
                    leagues = leagueRepository.findAllByDivisionIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(division);
                }
            } else {
                if (group != null) {
                    leagues = leagueRepository.findAllByGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(group);
                } else {
                    leagues = leagueRepository.findAllByOrderByRoundIdDescDivisionIdAscGroupIdAsc();
                }
            }
        }

        return leagues.stream().map(this::toSearchResultRepresentation).collect(Collectors.toList());
    }

    private LeagueRepresentation toSearchResultRepresentation(League league) {
        return new LeagueRepresentation(league.getId(), league.getRoundId(), league.getDivisionId(), league.getGroupId(), List.of());
    }

    private LeagueRepresentation toRepresentation(League league) {
        List<UserRepresentation> users = userRepository.findAllById(league.getUserIds()).stream().map(this::toRepresentation).collect(Collectors.toList());
        return new LeagueRepresentation(league.getId(), league.getRoundId(), league.getDivisionId(), league.getGroupId(), users);
    }

    private UserRepresentation toRepresentation(User user) {
        return new UserRepresentation(user.getId(), user.getUsername());
    }

    @Transactional
    public LeagueRepresentation saveLeague(LeagueRepresentation leagueRepresentation) {
        // TODO add validation of input parameters
        //  - exactly 10 users
        // - league doesn't exist already

        Set<Long> userIds = leagueRepresentation.getUsers().stream().map(UserRepresentation::getId).collect(Collectors.toSet());
        League league = new League(leagueRepresentation.getRound(), leagueRepresentation.getDivision(), leagueRepresentation.getGroup(), userIds);
        league = leagueRepository.save(league);
        return toRepresentation(league);
    }

    @Transactional
    public List<UserRepresentation> searchUsers(String searchString) {
        if (searchString.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> users = userRepository.findAllByUsernameIsStartingWithOrderByUsername(searchString);
        return users.stream().map(this::toRepresentation).collect(Collectors.toList());
    }


}
