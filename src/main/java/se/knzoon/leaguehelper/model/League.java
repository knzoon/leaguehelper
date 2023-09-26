package se.knzoon.leaguehelper.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer roundId;
    private Integer divisionId;
    private Integer groupId;

    @ElementCollection
    @Column(name = "userId")
    private Set<Long> userIds = new HashSet<>();

    public League() {
    }

    public League(Integer roundId, Integer divisionId, Integer groupId, Set<Long> userIds) {
        this.roundId = roundId;
        this.divisionId = divisionId;
        this.groupId = groupId;
        this.userIds = userIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }
}
