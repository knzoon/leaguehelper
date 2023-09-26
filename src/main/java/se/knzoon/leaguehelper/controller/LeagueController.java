package se.knzoon.leaguehelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.knzoon.leaguehelper.representation.GraphDataRepresentation;
import se.knzoon.leaguehelper.representation.LeagueRepresentation;
import se.knzoon.leaguehelper.representation.MigrationResultRepresentation;
import se.knzoon.leaguehelper.representation.UserRepresentation;
import se.knzoon.leaguehelper.service.MigrationService;
import se.knzoon.leaguehelper.service.TakeoverService;

import java.util.List;

@RestController
public class LeagueController {

    private final TakeoverService service;
    private final MigrationService migrationService;

    @Autowired
    public LeagueController(TakeoverService service, MigrationService migrationService) {
        this.service = service;
        this.migrationService = migrationService;
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/users")
    public List<UserRepresentation> searchUsers(@RequestParam(value = "searchString") String searchString) {
        List<UserRepresentation> users = service.searchUsers(searchString);
        return users;
    }


    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/graphPerDay")
    public GraphDataRepresentation getGraphDataForEachDay() {
        return service.getGraphDataForEachDay();
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/graphCumulative")
    public GraphDataRepresentation getGraphDataCumulative(@RequestParam(value = "leagueId") Long leagueId) {
        return service.getGraphDataCumulative(leagueId);
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/searchLeagues")
    public List<LeagueRepresentation> searchLeagues(@RequestParam(value = "roundId", required = false) Integer roundId, @RequestParam(value = "division", required = false) Integer division, @RequestParam(value = "group", required = false)Integer group) {
        return service.searchLeagues(roundId, division, group);
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @PostMapping("/api/league")
    public LeagueRepresentation saveLeague(@RequestBody LeagueRepresentation league) {
        return service.saveLeague(league);
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/migrateRevisits")
    public MigrationResultRepresentation migrateRevisitTakeovers(@RequestParam(value = "roundId", defaultValue = "151") Integer roundId) {
        return migrationService.migrateRevisitTakeovers(roundId);
    }

    @CrossOrigin(origins = "http://localhost:4300")
    @GetMapping("/api/migrateFirstTakeovers")
    public MigrationResultRepresentation migrateFirstTakeovers() {
        return migrationService.migrateFirstTakeovers(152);
    }

}
