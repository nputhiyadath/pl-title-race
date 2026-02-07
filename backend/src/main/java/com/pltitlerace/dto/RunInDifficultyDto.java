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
public class RunInDifficultyDto {
    private Long teamId;
    private String teamName;
    private Double difficultyScore;
    private Integer remainingFixtures;
    private List<FixtureDifficultyDto> fixtures;
}
