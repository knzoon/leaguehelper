package se.knzoon.leaguehelper.representation;

public class UserRepresentation {
    private final Long id;
    private final String username;

    public UserRepresentation(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
