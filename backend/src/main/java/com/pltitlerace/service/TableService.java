package com.pltitlerace.service;

import com.pltitlerace.dto.TeamStandingDto;
import com.pltitlerace.model.Team;
import com.pltitlerace.repository.LeagueTableSnapshotRepository;
import com.pltitlerace.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TeamRepository teamRepository;
    private final LeagueTableSnapshotRepository snapshotRepository;

    public List<TeamStandingDto> getCurrentTable() {
        List<Team> teams = teamRepository.findAllByOrderByPositionAsc();
        return teams.stream()
                .map(this::mapToStandingDto)
                .collect(Collectors.toList());
    }

    public List<TeamStandingDto> getTableByGameweek(Integer gameweek) {
        return snapshotRepository.findByGameweekOrderByPositionAsc(gameweek)
                .stream()
                .map(snapshot -> TeamStandingDto.builder()
                        .id(snapshot.getTeam().getId())
                        .name(snapshot.getTeam().getName())
                        .shortName(snapshot.getTeam().getShortName())
                        .crestUrl(snapshot.getTeam().getCrestUrl())
                        .position(snapshot.getPosition())
                        .previousPosition(snapshot.getPreviousPosition())
                        .positionChange(snapshot.getPreviousPosition() != null ?
                                snapshot.getPreviousPosition() - snapshot.getPosition() : 0)
                        .playedGames(snapshot.getPlayedGames())
                        .won(snapshot.getWon())
                        .draw(snapshot.getDraw())
                        .lost(snapshot.getLost())
                        .goalsFor(snapshot.getGoalsFor())
                        .goalsAgainst(snapshot.getGoalsAgainst())
                        .goalDifference(snapshot.getGoalDifference())
                        .points(snapshot.getPoints())
                        .form(snapshot.getForm())
                        .build())
                .collect(Collectors.toList());
    }

    private TeamStandingDto mapToStandingDto(Team team) {
        return TeamStandingDto.builder()
                .id(team.getId())
                .name(team.getName())
                .shortName(team.getShortName())
                .crestUrl(team.getCrestUrl())
                .position(team.getPosition())
                .playedGames(team.getPlayedGames())
                .won(team.getWon())
                .draw(team.getDraw())
                .lost(team.getLost())
                .goalsFor(team.getGoalsFor())
                .goalsAgainst(team.getGoalsAgainst())
                .goalDifference(team.getGoalDifference())
                .points(team.getPoints())
                .form(team.getForm())
                .build();
    }
}
