package se.knzoon.leaguehelper.representation;

import java.util.List;

public class LeagueRepresentation {
    private final Long id;
    private final Integer round;
    private final Integer division;
    private final Integer group;
    private final List<UserRepresentation> users;


    public LeagueRepresentation(Long id, Integer round, Integer division, Integer group, List<UserRepresentation> users) {
        this.id = id;
        this.round = round;
        this.division = division;
        this.group = group;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public Integer getRound() {
        return round;
    }

    public Integer getDivision() {
        return division;
    }

    public Integer getGroup() {
        return group;
    }

    public List<UserRepresentation> getUsers() {
        return users;
    }
}
