package com.pltitlerace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStandingDto {
    private Long id;
    private String name;
    private String shortName;
    private String crestUrl;
    private Integer position;
    private Integer previousPosition;
    private Integer playedGames;
    private Integer won;
    private Integer draw;
    private Integer lost;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer goalDifference;
    private Integer points;
    private String form;
    private Integer positionChange;
}
