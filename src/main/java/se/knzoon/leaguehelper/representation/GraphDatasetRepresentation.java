package se.knzoon.leaguehelper.representation;

import java.util.List;

public class GraphDatasetRepresentation {
    private final String label;
    private final List<GraphDatapointRepresentation> data;
    private final String borderColor;
    private final String country;
    private final Integer currentPoints;
    private final Integer currentPph;

    public GraphDatasetRepresentation(String label, List<GraphDatapointRepresentation> data, String borderColor, String country, Integer currentPoints, Integer currentPph) {
        this.label = label;
        this.data = data;
        this.borderColor = borderColor;
        this.country = country;
        this.currentPoints = currentPoints;
        this.currentPph = currentPph;
    }

    public String getLabel() {
        return label;
    }

    public List<GraphDatapointRepresentation> getData() {
        return data;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getCountry() {
        return country;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public Integer getCurrentPph() {
        return currentPph;
    }
}
