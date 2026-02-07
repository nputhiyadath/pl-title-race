package com.pltitlerace.service;

import com.pltitlerace.dto.TeamProbabilityDto;
import com.pltitlerace.dto.TitleRaceProjectionDto;
import com.pltitlerace.model.Fixture;
import com.pltitlerace.model.SimulationResult;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.FixtureRepository;
import com.pltitlerace.repository.SimulationResultRepository;
import com.pltitlerace.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;
    private final SimulationResultRepository simulationResultRepository;
    private static final Random random = new Random();

    public TitleRaceProjectionDto getCurrentProjection() {
        Integer currentGameweek = getCurrentGameweek();

        // Check if we have any simulation results for the current gameweek
        long resultCount = simulationResultRepository.count();

        // If no simulation results exist, run a simulation automatically
        if (resultCount == 0) {
            return runSimulation(10000);
        }

        List<Team> topTeams = teamRepository.findByPositionLessThanEqualOrderByPositionAsc(6);

        List<TeamProbabilityDto> probabilities = topTeams.stream()
                .map(team -> {
                    SimulationResult latestResult = simulationResultRepository
                            .findByTeamAndGameweek(team, currentGameweek)
                            .orElse(null);

                    return TeamProbabilityDto.builder()
                            .teamId(team.getId())
                            .teamName(team.getName())
                            .shortName(team.getShortName())
                            .titleProbability(latestResult != null ? latestResult.getTitleProbability() : 0.0)
                            .top4Probability(latestResult != null ? latestResult.getTop4Probability() : 0.0)
                            .currentPoints(team.getPoints())
                            .averageFinalPoints(latestResult != null ? latestResult.getAverageFinalPoints() : 0.0)
                            .minFinalPoints(latestResult != null ? latestResult.getMinFinalPoints() : 0)
                            .maxFinalPoints(latestResult != null ? latestResult.getMaxFinalPoints() : 0)
                            .build();
                })
                .collect(Collectors.toList());

        return TitleRaceProjectionDto.builder()
                .currentGameweek(currentGameweek)
                .probabilities(probabilities)
                .simulationRuns(10000)
                .narrative("Simulation data based on current standings and remaining fixtures.")
                .build();
    }

    public TitleRaceProjectionDto runSimulation(Integer runs) {
        List<Team> allTeams = teamRepository.findAllByOrderByPositionAsc();
        List<Fixture> remainingFixtures = fixtureRepository.findByStatus(Fixture.FixtureStatus.SCHEDULED);
        Integer currentGameweek = getCurrentGameweek();

        Map<Long, Integer> titleWins = new HashMap<>();
        Map<Long, Integer> top4Finishes = new HashMap<>();
        Map<Long, List<Integer>> finalPointsDistribution = new HashMap<>();

        allTeams.forEach(team -> {
            titleWins.put(team.getId(), 0);
            top4Finishes.put(team.getId(), 0);
            finalPointsDistribution.put(team.getId(), new ArrayList<>());
        });

        for (int i = 0; i < runs; i++) {
            Map<Long, Integer> simulatedPoints = simulateRemainingFixtures(allTeams, remainingFixtures);

            List<Map.Entry<Long, Integer>> standings = simulatedPoints.entrySet().stream()
                    .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());

            Long winnerId = standings.get(0).getKey();
            titleWins.put(winnerId, titleWins.get(winnerId) + 1);

            for (int pos = 0; pos < Math.min(4, standings.size()); pos++) {
                Long teamId = standings.get(pos).getKey();
                top4Finishes.put(teamId, top4Finishes.get(teamId) + 1);
            }

            simulatedPoints.forEach((teamId, points) ->
                finalPointsDistribution.get(teamId).add(points)
            );
        }

        List<TeamProbabilityDto> probabilities = allTeams.stream()
                .filter(team -> team.getPosition() <= 6)
                .map(team -> {
                    double titleProb = titleWins.get(team.getId()) / (double) runs;
                    double top4Prob = top4Finishes.get(team.getId()) / (double) runs;

                    List<Integer> pointsList = finalPointsDistribution.get(team.getId());
                    double avgPoints = pointsList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    int minPoints = pointsList.stream().mapToInt(Integer::intValue).min().orElse(0);
                    int maxPoints = pointsList.stream().mapToInt(Integer::intValue).max().orElse(0);

                    SimulationResult result = new SimulationResult();
                    result.setTeam(team);
                    result.setGameweek(currentGameweek);
                    result.setTitleProbability(titleProb);
                    result.setTop4Probability(top4Prob);
                    result.setSimulationRuns(runs);
                    result.setAverageFinalPoints(avgPoints);
                    result.setMinFinalPoints(minPoints);
                    result.setMaxFinalPoints(maxPoints);
                    result.setCalculatedAt(LocalDateTime.now());

                    simulationResultRepository.save(result);

                    return TeamProbabilityDto.builder()
                            .teamId(team.getId())
                            .teamName(team.getName())
                            .shortName(team.getShortName())
                            .titleProbability(titleProb)
                            .top4Probability(top4Prob)
                            .currentPoints(team.getPoints())
                            .averageFinalPoints(avgPoints)
                            .minFinalPoints(minPoints)
                            .maxFinalPoints(maxPoints)
                            .build();
                })
                .sorted(Comparator.comparing(TeamProbabilityDto::getTitleProbability).reversed())
                .collect(Collectors.toList());

        return TitleRaceProjectionDto.builder()
                .currentGameweek(currentGameweek)
                .probabilities(probabilities)
                .simulationRuns(runs)
                .narrative("Monte Carlo simulation completed with " + runs + " runs.")
                .build();
    }

    public TitleRaceProjectionDto runConstrainedSimulation(Integer runs, Map<Long, String> constrainedResults) {
        List<Team> allTeams = teamRepository.findAllByOrderByPositionAsc();
        List<Fixture> remainingFixtures = fixtureRepository.findByStatus(Fixture.FixtureStatus.SCHEDULED);
        Integer currentGameweek = getCurrentGameweek();

        Map<Long, Integer> titleWins = new HashMap<>();
        Map<Long, Integer> top4Finishes = new HashMap<>();
        Map<Long, List<Integer>> finalPointsDistribution = new HashMap<>();

        allTeams.forEach(team -> {
            titleWins.put(team.getId(), 0);
            top4Finishes.put(team.getId(), 0);
            finalPointsDistribution.put(team.getId(), new ArrayList<>());
        });

        for (int i = 0; i < runs; i++) {
            Map<Long, Integer> simulatedPoints = simulateRemainingFixturesWithConstraints(
                allTeams, remainingFixtures, constrainedResults);

            List<Map.Entry<Long, Integer>> standings = simulatedPoints.entrySet().stream()
                    .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());

            Long winnerId = standings.get(0).getKey();
            titleWins.put(winnerId, titleWins.get(winnerId) + 1);

            for (int pos = 0; pos < Math.min(4, standings.size()); pos++) {
                Long teamId = standings.get(pos).getKey();
                top4Finishes.put(teamId, top4Finishes.get(teamId) + 1);
            }

            simulatedPoints.forEach((teamId, points) ->
                finalPointsDistribution.get(teamId).add(points)
            );
        }

        List<TeamProbabilityDto> probabilities = allTeams.stream()
                .filter(team -> team.getPosition() <= 6)
                .map(team -> {
                    double titleProb = titleWins.get(team.getId()) / (double) runs;
                    double top4Prob = top4Finishes.get(team.getId()) / (double) runs;

                    List<Integer> pointsList = finalPointsDistribution.get(team.getId());
                    double avgPoints = pointsList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    int minPoints = pointsList.stream().mapToInt(Integer::intValue).min().orElse(0);
                    int maxPoints = pointsList.stream().mapToInt(Integer::intValue).max().orElse(0);

                    return TeamProbabilityDto.builder()
                            .teamId(team.getId())
                            .teamName(team.getName())
                            .shortName(team.getShortName())
                            .titleProbability(titleProb)
                            .top4Probability(top4Prob)
                            .currentPoints(team.getPoints())
                            .averageFinalPoints(avgPoints)
                            .minFinalPoints(minPoints)
                            .maxFinalPoints(maxPoints)
                            .build();
                })
                .sorted(Comparator.comparing(TeamProbabilityDto::getTitleProbability).reversed())
                .collect(Collectors.toList());

        return TitleRaceProjectionDto.builder()
                .currentGameweek(currentGameweek)
                .probabilities(probabilities)
                .simulationRuns(runs)
                .narrative("Constrained simulation with " + constrainedResults.size() + " pre-determined results.")
                .build();
    }

    private Map<Long, Integer> simulateRemainingFixtures(List<Team> teams, List<Fixture> fixtures) {
        return simulateRemainingFixturesWithConstraints(teams, fixtures, new HashMap<>());
    }

    private Map<Long, Integer> simulateRemainingFixturesWithConstraints(
            List<Team> teams, List<Fixture> fixtures, Map<Long, String> constrainedResults) {
        Map<Long, Integer> points = new HashMap<>();
        teams.forEach(team -> points.put(team.getId(), team.getPoints() != null ? team.getPoints() : 0));

        for (Fixture fixture : fixtures) {
            Team homeTeam = fixture.getHomeTeam();
            Team awayTeam = fixture.getAwayTeam();

            // Check if this fixture has a constrained result
            String constrainedResult = constrainedResults.get(fixture.getId());

            if (constrainedResult != null) {
                // Apply the constrained result
                switch (constrainedResult) {
                    case "H":
                        points.put(homeTeam.getId(), points.get(homeTeam.getId()) + 3);
                        break;
                    case "D":
                        points.put(homeTeam.getId(), points.get(homeTeam.getId()) + 1);
                        points.put(awayTeam.getId(), points.get(awayTeam.getId()) + 1);
                        break;
                    case "A":
                        points.put(awayTeam.getId(), points.get(awayTeam.getId()) + 3);
                        break;
                }
            } else {
                // Use probabilistic simulation for unconstrained fixtures
                double homeWinProb = calculateWinProbability(homeTeam, awayTeam, true);
                double drawProb = 0.25;
                double awayWinProb = 1.0 - homeWinProb - drawProb;

                double rand = random.nextDouble();

                if (rand < homeWinProb) {
                    points.put(homeTeam.getId(), points.get(homeTeam.getId()) + 3);
                } else if (rand < homeWinProb + drawProb) {
                    points.put(homeTeam.getId(), points.get(homeTeam.getId()) + 1);
                    points.put(awayTeam.getId(), points.get(awayTeam.getId()) + 1);
                } else {
                    points.put(awayTeam.getId(), points.get(awayTeam.getId()) + 3);
                }
            }
        }

        return points;
    }

    private double calculateWinProbability(Team homeTeam, Team awayTeam, boolean isHome) {
        double homeStrength = homeTeam.getStrength() != null ? homeTeam.getStrength() : 50.0;
        double awayStrength = awayTeam.getStrength() != null ? awayTeam.getStrength() : 50.0;

        double homeAdvantage = isHome ? 5.0 : 0.0;
        double formFactor = calculateFormFactor(homeTeam) - calculateFormFactor(awayTeam);

        double strengthDiff = (homeStrength - awayStrength + homeAdvantage + formFactor) / 100.0;

        return 0.5 + (strengthDiff * 0.3);
    }

    private double calculateFormFactor(Team team) {
        if (team.getForm() == null || team.getForm().isEmpty()) return 0.0;

        int recentPoints = 0;
        String form = team.getForm();
        for (char c : form.toCharArray()) {
            if (c == 'W') recentPoints += 3;
            else if (c == 'D') recentPoints += 1;
        }

        return (recentPoints / (double) (form.length() * 3)) * 10.0;
    }

    private Integer getCurrentGameweek() {
        return 20;
    }
}
