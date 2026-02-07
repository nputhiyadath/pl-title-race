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
public class TitleRaceProjectionDto {
    private Integer currentGameweek;
    private List<TeamProbabilityDto> probabilities;
    private Integer simulationRuns;
    private String narrative;
}
