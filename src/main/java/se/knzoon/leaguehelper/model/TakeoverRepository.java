package se.knzoon.leaguehelper.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TakeoverRepository extends JpaRepository<Takeover, Long> {
    List<Takeover> findAllByRoundIdAndUserInOrderByTakeoverTime(Integer roundId, Set<User> users);

    List<Takeover> findAllByIdIn(Set<Long> takeoverIds);

    List<Takeover> findAllByRoundIdAndZoneIdOrderById(Integer roundId, Long zoneId);

    @Query(value = "select distinct t.zone_id as zoneId from takeover t where t.round_id = :roundId and t.previous_user_id = t.user_id", nativeQuery = true)
    List<ZoneView> findZonesHavingARevisitForRound(@Param("roundId") Integer roundId);

    @Query(value = "select ft.id as firstTakeoverId, min(st.id) as secondTakeoverId from takeover ft inner join takeover st on st.round_id = ft.round_id and st.zone_id = ft.zone_id and st.previous_user_id = ft.user_id where ft.round_id = :roundId and ft.previous_user_id is null and ft.lost_time is null and st.user_id <> st.previous_user_id group by ft.id", nativeQuery = true)
    List<TakeoverPairView> findFirstTwoTakeoversForRound(@Param("roundId") Integer roundId);


}
