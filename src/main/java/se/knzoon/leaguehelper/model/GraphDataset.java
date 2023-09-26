package se.knzoon.leaguehelper.model;

import se.knzoon.leaguehelper.representation.GraphDataRepresentation;
import se.knzoon.leaguehelper.representation.GraphDatapointRepresentation;
import se.knzoon.leaguehelper.representation.GraphDatasetRepresentation;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GraphDataset {

    private final int days;
    private final String userName;
    private final String color;
    private final String country;
    private List<Integer> cumulativePointsPerDay = new ArrayList<>();
    private List<Integer> pphPerDay = new ArrayList<>();


    public GraphDataset(int days, String userName, String color, String country) {
        this.days = days;
        this.userName = userName;
        this.color = color;
        this.country = country;
    }

    public void addCummulativePoints(List<Integer> pointsPerDay) {
        if  (pointsPerDay.size() == days) {
            cumulativePointsPerDay.addAll(pointsPerDay);
        }
    }

    public void addPph(List<Integer> pphPerDay) {
        if (pphPerDay.size() == days) {
            this.pphPerDay.addAll(pphPerDay);
        }
    }

    public Integer currentPoints() {
        if (cumulativePointsPerDay.isEmpty()) {
            return 0;
        }

        return cumulativePointsPerDay.get(cumulativePointsPerDay.size() - 1);
    }

    public Integer currentPph() {
        if (pphPerDay.isEmpty()) {
            return 0;
        }

        return pphPerDay.stream().collect(Collectors.summingInt(Integer::intValue));
    }

    public GraphDatasetRepresentation toRepresentation() {
        return new GraphDatasetRepresentation(userName, datapointToRepresentations(), color, country, currentPoints(), currentPph());
    }

    private List<GraphDatapointRepresentation> datapointToRepresentations() {
        List<GraphDatapointRepresentation> points = new ArrayList<>();

        for (int i=0; i < days; i++) {
            points.add(new GraphDatapointRepresentation(getDay(i), cumulativePoints(i), pph(i)));
        }

        return points;
    }

    private String getDay(int i) {
        return Integer.valueOf(i + 1).toString();
    }

    private Integer cumulativePoints(int i) {
        if (cumulativePointsPerDay.isEmpty()) {
            return 0;
        }
        return cumulativePointsPerDay.get(i);
    }

    private Integer pph(int i) {
        if (pphPerDay.isEmpty()) {
            return 0;
        }
        return pphPerDay.get(i);
    }
/*
    public GraphDataRepresentation getGraphDataCumulative() {
        Set<User> users = getUsersInvolved();
        Map<Long, List<Takeover>> takeoversPerUser = getTakeoversForUsers(users);
        int numberOfDaysInRoundYet = numberOfDaysInRoundYet(takeoversPerUser.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));

        ZonedDateTime now = Instant.now().atZone(ZoneId.of("UTC"));
        List<GraphDatasetRepresentation> datasets = new ArrayList<>();
        int i = 0;

        for (User user : users.stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList())) {
            List<Takeover> takeovers = takeoversPerUser.get(user.getId());
            logger.info("user {} has {} takeovers", user.getUsername(), takeovers.size());
            List<Integer> pointsPerDay = calculatePointsCumulative(takeovers, now, numberOfDaysInRoundYet);
            List<GraphDatapointRepresentation> graphDatapointRepresentations = datapointToRepresentations(pointsPerDay);
            datasets.add(new GraphDatasetRepresentation(user.getUsername(), graphDatapointRepresentations, colors.get(i++), "unknown", pointsPerDay.get(pointsPerDay.size() - 1), 0));
        }


        return new GraphDataRepresentation(datasets.stream().sorted(Comparator.comparing(GraphDatasetRepresentation::getCurrentPoints).reversed()).collect(Collectors.toList()));
    }

    private List<GraphDatapointRepresentation> datapointToRepresentations(List<Integer> pointsPerDay) {
        int currentDay = 1;
        List<GraphDatapointRepresentation> representations = new ArrayList<>();
        for (Integer points : pointsPerDay) {
            representations.add(new GraphDatapointRepresentation(Integer.valueOf(currentDay++).toString(), points, 0));
        }
        return representations;
    }
*/
}
