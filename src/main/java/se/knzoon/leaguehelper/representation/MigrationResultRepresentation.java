package se.knzoon.leaguehelper.representation;

public class MigrationResultRepresentation {
    private final String message;
    private final Integer nrofMigrated;

    public MigrationResultRepresentation(String message, Integer nrofMigrated) {
        this.message = message;
        this.nrofMigrated = nrofMigrated;
    }

    public String getMessage() {
        return message;
    }

    public Integer getNrofMigrated() {
        return nrofMigrated;
    }
}
