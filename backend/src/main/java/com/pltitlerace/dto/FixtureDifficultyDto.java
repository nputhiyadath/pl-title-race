package com.pltitlerace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixtureDifficultyDto {
    private Long fixtureId;
    private String opponent;
    private Boolean isHome;
    private LocalDateTime matchDate;
    private Double difficulty;
    private Integer opponentPosition;
}
