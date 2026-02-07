package com.pltitlerace.service;

import com.pltitlerace.dto.FixtureDifficultyDto;
import com.pltitlerace.dto.RunInDifficultyDto;
import com.pltitlerace.model.Fixture;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.FixtureRepository;
import com.pltitlerace.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunInService {

    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;

    public List<RunInDifficultyDto> calculateRunInDifficulty() {
        List<Team> topTeams = teamRepository.findByPositionLessThanEqualOrderByPositionAsc(6);

        return topTeams.stream()
                .map(this::calculateTeamRunInDifficulty)
                .sorted(Comparator.comparing(RunInDifficultyDto::getDifficultyScore).reversed())
                .collect(Collectors.toList());
    }

    public RunInDifficultyDto calculateTeamRunInDifficulty(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return calculateTeamRunInDifficulty(team);
    }

    private RunInDifficultyDto calculateTeamRunInDifficulty(Team team) {
        List<Fixture> remainingFixtures = fixtureRepository.findRemainingFixturesByTeam(team);

        List<FixtureDifficultyDto> fixtureDifficulties = remainingFixtures.stream()
                .map(fixture -> {
                    boolean isHome = fixture.getHomeTeam().getId().equals(team.getId());
                    Team opponent = isHome ? fixture.getAwayTeam() : fixture.getHomeTeam();

                    double difficulty = calculateFixtureDifficulty(opponent, isHome);

                    return FixtureDifficultyDto.builder()
                            .fixtureId(fixture.getId())
                            .opponent(opponent.getShortName())
                            .isHome(isHome)
                            .matchDate(fixture.getMatchDate())
                            .difficulty(difficulty)
                            .opponentPosition(opponent.getPosition())
                            .build();
                })
                .collect(Collectors.toList());

        double totalDifficulty = fixtureDifficulties.stream()
                .mapToDouble(FixtureDifficultyDto::getDifficulty)
                .sum();

        double normalizedDifficulty = fixtureDifficulties.isEmpty() ? 0.0 :
                (totalDifficulty / fixtureDifficulties.size()) * 10;

        return RunInDifficultyDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .difficultyScore(normalizedDifficulty)
                .remainingFixtures(remainingFixtures.size())
                .fixtures(fixtureDifficulties)
                .build();
    }

    private double calculateFixtureDifficulty(Team opponent, boolean isHome) {
        double opponentStrength = opponent.getStrength() != null ? opponent.getStrength() : 50.0;
        double locationMultiplier = isHome ? 0.8 : 1.2;

        double positionFactor = (21 - (opponent.getPosition() != null ? opponent.getPosition() : 10)) / 20.0;

        return (opponentStrength / 100.0) * locationMultiplier * positionFactor;
    }
}
