package se.knzoon.leaguehelper.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {

    List<League> findAllByRoundIdAndDivisionIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer roundId, Integer divisionId, Integer groupId);
    List<League> findAllByRoundIdAndDivisionIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer roundId, Integer divisionId);
    List<League> findAllByRoundIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer roundId, Integer groupId);
    List<League> findAllByRoundIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer roundId);
    List<League> findAllByDivisionIdAndGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer divisionId, Integer groupId);
    List<League> findAllByDivisionIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer divisionId);
    List<League> findAllByGroupIdOrderByRoundIdDescDivisionIdAscGroupIdAsc(Integer groupId);
    List<League> findAllByOrderByRoundIdDescDivisionIdAscGroupIdAsc();
}
