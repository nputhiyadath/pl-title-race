package com.pltitlerace.util;

import com.pltitlerace.model.Fixture;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.FixtureRepository;
import com.pltitlerace.repository.TeamRepository;
import com.pltitlerace.service.FootballDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;
    private final FootballDataService footballDataService;

    @Value("${football.api.api-key}")
    private String apiKey;

    @Override
    public void run(String... args) {
        if (teamRepository.count() == 0) {
            log.info("Initializing data...");

            // Try to load from API if API key is configured
            if (apiKey != null && !apiKey.isEmpty()) {
                try {
                    log.info("Loading data from football-data.org API...");
                    footballDataService.loadStandingsFromApi();
                    footballDataService.loadFixturesFromApi();
                    log.info("Data loaded successfully from API");
                    return;
                } catch (Exception e) {
                    log.warn("Failed to load from API, falling back to sample data: {}", e.getMessage());
                }
            } else {
                log.info("No API key configured, using sample data");
            }

            // Fall back to sample data if API fails or is not configured
            initializeTeams();
            initializeFixtures();
            log.info("Sample data initialized successfully");
        }
    }

    private void initializeTeams() {
        List<Team> teams = Arrays.asList(
                createTeam(1L, 57L, "Arsenal FC", "Arsenal", 85.0, 1, 20, 13, 4, 3, 45, 20, 25, 43, "WWDWW"),
                createTeam(2L, 65L, "Manchester City FC", "Man City", 90.0, 2, 20, 12, 5, 3, 48, 18, 30, 41, "DWWWD"),
                createTeam(3L, 64L, "Liverpool FC", "Liverpool", 88.0, 3, 20, 12, 4, 4, 42, 22, 20, 40, "WLWWW"),
                createTeam(4L, 73L, "Tottenham Hotspur FC", "Spurs", 75.0, 4, 20, 10, 5, 5, 38, 28, 10, 35, "LWDWL"),
                createTeam(5L, 66L, "Manchester United FC", "Man Utd", 78.0, 5, 20, 9, 6, 5, 32, 26, 6, 33, "WLDWD"),
                createTeam(6L, 61L, "Chelsea FC", "Chelsea", 80.0, 6, 20, 9, 5, 6, 35, 28, 7, 32, "LWWDL")
        );

        teamRepository.saveAll(teams);
        log.info("Initialized {} teams", teams.size());
    }

    private Team createTeam(Long id, Long externalId, String name, String shortName, Double strength,
                           Integer position, Integer played, Integer won, Integer draw, Integer lost,
                           Integer gf, Integer ga, Integer gd, Integer points, String form) {
        Team team = new Team();
        team.setId(id);
        team.setExternalId(externalId);
        team.setName(name);
        team.setShortName(shortName);
        team.setStrength(strength);
        team.setPosition(position);
        team.setPlayedGames(played);
        team.setWon(won);
        team.setDraw(draw);
        team.setLost(lost);
        team.setGoalsFor(gf);
        team.setGoalsAgainst(ga);
        team.setGoalDifference(gd);
        team.setPoints(points);
        team.setForm(form);
        return team;
    }

    private void initializeFixtures() {
        Team arsenal = teamRepository.findById(1L).orElseThrow();
        Team manCity = teamRepository.findById(2L).orElseThrow();
        Team liverpool = teamRepository.findById(3L).orElseThrow();
        Team spurs = teamRepository.findById(4L).orElseThrow();

        List<Fixture> fixtures = Arrays.asList(
                createFixture(1L, 1001L, arsenal, manCity, LocalDateTime.now().plusDays(7), 21, Fixture.FixtureStatus.SCHEDULED),
                createFixture(2L, 1002L, liverpool, spurs, LocalDateTime.now().plusDays(7), 21, Fixture.FixtureStatus.SCHEDULED),
                createFixture(3L, 1003L, manCity, liverpool, LocalDateTime.now().plusDays(14), 22, Fixture.FixtureStatus.SCHEDULED),
                createFixture(4L, 1004L, spurs, arsenal, LocalDateTime.now().plusDays(14), 22, Fixture.FixtureStatus.SCHEDULED)
        );

        fixtureRepository.saveAll(fixtures);
        log.info("Initialized {} fixtures", fixtures.size());
    }

    private Fixture createFixture(Long id, Long externalId, Team home, Team away,
                                 LocalDateTime matchDate, Integer gameweek, Fixture.FixtureStatus status) {
        Fixture fixture = new Fixture();
        fixture.setId(id);
        fixture.setExternalId(externalId);
        fixture.setHomeTeam(home);
        fixture.setAwayTeam(away);
        fixture.setMatchDate(matchDate);
        fixture.setGameweek(gameweek);
        fixture.setStatus(status);
        return fixture;
    }
}
