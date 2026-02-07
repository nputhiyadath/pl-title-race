package com.pltitlerace.service;

import com.pltitlerace.dto.MatchSummaryDto;
import com.pltitlerace.dto.TeamFormDto;
import com.pltitlerace.model.MatchResult;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.MatchResultRepository;
import com.pltitlerace.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MatchResultRepository matchResultRepository;

    public TeamFormDto getTeamForm(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<MatchResult> recentMatches = matchResultRepository
                .findByTeamOrderByRecordedAtDesc(team)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        List<MatchSummaryDto> matchSummaries = recentMatches.stream()
                .map(result -> mapToMatchSummary(result, team))
                .collect(Collectors.toList());

        int pointsLastFive = calculatePointsLastFive(recentMatches, team);
        double avgGoalsScored = calculateAverageGoalsScored(recentMatches, team);
        double avgGoalsConceded = calculateAverageGoalsConceded(recentMatches, team);

        return TeamFormDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .form(team.getForm())
                .recentMatches(matchSummaries)
                .pointsLastFive(pointsLastFive)
                .averageGoalsScored(avgGoalsScored)
                .averageGoalsConceded(avgGoalsConceded)
                .build();
    }

    private MatchSummaryDto mapToMatchSummary(MatchResult result, Team team) {
        boolean isHome = result.getFixture().getHomeTeam().getId().equals(team.getId());
        String resultType = getResultType(result, team);

        return MatchSummaryDto.builder()
                .fixtureId(result.getFixture().getId())
                .homeTeam(result.getFixture().getHomeTeam().getShortName())
                .awayTeam(result.getFixture().getAwayTeam().getShortName())
                .homeScore(result.getHomeScore())
                .awayScore(result.getAwayScore())
                .matchDate(result.getFixture().getMatchDate())
                .result(resultType)
                .build();
    }

    private String getResultType(MatchResult result, Team team) {
        boolean isHome = result.getFixture().getHomeTeam().getId().equals(team.getId());

        if (result.getResultType() == MatchResult.ResultType.DRAW) {
            return "D";
        }

        if ((isHome && result.getResultType() == MatchResult.ResultType.HOME_WIN) ||
            (!isHome && result.getResultType() == MatchResult.ResultType.AWAY_WIN)) {
            return "W";
        }

        return "L";
    }

    private int calculatePointsLastFive(List<MatchResult> matches, Team team) {
        return matches.stream()
                .mapToInt(result -> {
                    String resultType = getResultType(result, team);
                    if ("W".equals(resultType)) return 3;
                    if ("D".equals(resultType)) return 1;
                    return 0;
                })
                .sum();
    }

    private double calculateAverageGoalsScored(List<MatchResult> matches, Team team) {
        if (matches.isEmpty()) return 0.0;

        return matches.stream()
                .mapToInt(result -> {
                    boolean isHome = result.getFixture().getHomeTeam().getId().equals(team.getId());
                    return isHome ? result.getHomeScore() : result.getAwayScore();
                })
                .average()
                .orElse(0.0);
    }

    private double calculateAverageGoalsConceded(List<MatchResult> matches, Team team) {
        if (matches.isEmpty()) return 0.0;

        return matches.stream()
                .mapToInt(result -> {
                    boolean isHome = result.getFixture().getHomeTeam().getId().equals(team.getId());
                    return isHome ? result.getAwayScore() : result.getHomeScore();
                })
                .average()
                .orElse(0.0);
    }
}
