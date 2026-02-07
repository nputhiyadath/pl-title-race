package com.pltitlerace.service;

import com.pltitlerace.client.FootballDataClient;
import com.pltitlerace.client.dto.MatchesResponse;
import com.pltitlerace.client.dto.StandingsResponse;
import com.pltitlerace.model.Fixture;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.FixtureRepository;
import com.pltitlerace.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FootballDataService {

    private final FootballDataClient footballDataClient;
    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;

    @Transactional
    public void loadStandingsFromApi() {
        try {
            log.info("Loading standings from football-data.org API...");
            StandingsResponse response = footballDataClient.getStandings();

            if (response == null || response.getStandings() == null || response.getStandings().isEmpty()) {
                log.warn("No standings data received from API");
                return;
            }

            // Get the main standings table (usually first entry)
            List<StandingsResponse.TableEntry> tableEntries = response.getStandings().get(0).getTable();

            // Calculate team strengths based on position and form
            for (StandingsResponse.TableEntry entry : tableEntries) {
                Team team = new Team();
                team.setExternalId(entry.getTeam().getId());
                team.setName(entry.getTeam().getName());
                team.setShortName(entry.getTeam().getShortName());
                team.setPosition(entry.getPosition());
                team.setPlayedGames(entry.getPlayedGames());
                team.setWon(entry.getWon());
                team.setDraw(entry.getDraw());
                team.setLost(entry.getLost());
                team.setGoalsFor(entry.getGoalsFor());
                team.setGoalsAgainst(entry.getGoalsAgainst());
                team.setGoalDifference(entry.getGoalDifference());
                team.setPoints(entry.getPoints());
                team.setForm(entry.getForm() != null ? entry.getForm() : "");

                // Calculate strength based on position (1st = 95, last = 60)
                double strength = 100 - (entry.getPosition() * 2.0);
                team.setStrength(Math.max(60.0, Math.min(95.0, strength)));

                teamRepository.save(team);
            }

            log.info("Successfully loaded {} teams from API", tableEntries.size());
        } catch (Exception e) {
            log.error("Error loading standings from API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load standings from API", e);
        }
    }

    @Transactional
    public void loadFixturesFromApi() {
        try {
            log.info("Loading scheduled fixtures from football-data.org API...");
            MatchesResponse response = footballDataClient.getMatches("SCHEDULED");

            if (response == null || response.getMatches() == null || response.getMatches().isEmpty()) {
                log.warn("No fixture data received from API");
                return;
            }

            // Create a map of external team IDs to our Team entities
            Map<Long, Team> teamMap = new HashMap<>();
            List<Team> allTeams = teamRepository.findAll();
            for (Team team : allTeams) {
                teamMap.put(team.getExternalId(), team);
            }

            int savedCount = 0;
            for (MatchesResponse.Match match : response.getMatches()) {
                Team homeTeam = teamMap.get(match.getHomeTeam().getId());
                Team awayTeam = teamMap.get(match.getAwayTeam().getId());

                if (homeTeam == null || awayTeam == null) {
                    log.debug("Skipping match {}: Team not found in database", match.getId());
                    continue;
                }

                Fixture fixture = new Fixture();
                fixture.setExternalId(match.getId());
                fixture.setHomeTeam(homeTeam);
                fixture.setAwayTeam(awayTeam);
                fixture.setMatchDate(match.getUtcDate());
                fixture.setGameweek(match.getMatchday());
                fixture.setStatus(mapStatus(match.getStatus()));

                fixtureRepository.save(fixture);
                savedCount++;
            }

            log.info("Successfully loaded {} fixtures from API", savedCount);
        } catch (Exception e) {
            log.error("Error loading fixtures from API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load fixtures from API", e);
        }
    }

    private Fixture.FixtureStatus mapStatus(String apiStatus) {
        return switch (apiStatus) {
            case "SCHEDULED", "TIMED", "IN_PLAY", "PAUSED" -> Fixture.FixtureStatus.SCHEDULED;
            case "FINISHED" -> Fixture.FixtureStatus.FINISHED;
            case "POSTPONED" -> Fixture.FixtureStatus.POSTPONED;
            case "CANCELLED" -> Fixture.FixtureStatus.CANCELLED;
            default -> Fixture.FixtureStatus.SCHEDULED;
        };
    }
}
