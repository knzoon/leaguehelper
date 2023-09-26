package se.knzoon.leaguehelper.representation;

import java.util.List;

public class GraphDataRepresentation {
    private final List<GraphDatasetRepresentation> datasets;

    public GraphDataRepresentation(List<GraphDatasetRepresentation> datasets) {
        this.datasets = datasets;
    }

    public List<GraphDatasetRepresentation> getDatasets() {
        return datasets;
    }
}
