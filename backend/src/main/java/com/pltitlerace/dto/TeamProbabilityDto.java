package com.pltitlerace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamProbabilityDto {
    private Long teamId;
    private String teamName;
    private String shortName;
    private Double titleProbability;
    private Double top4Probability;
    private Double previousTitleProbability;
    private Double probabilityChange;
    private Integer currentPoints;
    private Double averageFinalPoints;
    private Integer minFinalPoints;
    private Integer maxFinalPoints;
}
