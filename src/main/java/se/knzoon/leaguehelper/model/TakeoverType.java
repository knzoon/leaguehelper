package se.knzoon.leaguehelper.model;

public enum TakeoverType {
    TAKEOVER("T"), ASSIST("A");

    private String type;

    TakeoverType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
