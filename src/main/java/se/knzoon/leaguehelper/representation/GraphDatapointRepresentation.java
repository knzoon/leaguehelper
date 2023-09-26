package se.knzoon.leaguehelper.representation;

public class GraphDatapointRepresentation {
    private final String day;
    private final Integer points;
    private final Integer pph;

    public GraphDatapointRepresentation(String day, Integer points, Integer pph) {
        this.day = day;
        this.points = points;
        this.pph = pph;
    }

    public String getDay() {
        return day;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getPph() {
        return pph;
    }
}
