package com.pltitlerace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamFormDto {
    private Long teamId;
    private String teamName;
    private String form;
    private List<MatchSummaryDto> recentMatches;
    private Integer pointsLastFive;
    private Double averageGoalsScored;
    private Double averageGoalsConceded;
}
