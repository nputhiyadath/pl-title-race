package com.pltitlerace.dto;

import lombok.Data;

@Data
public class FixtureResultDto {
    private Long fixtureId;
    private String result; // "H" for home win, "D" for draw, "A" for away win
}
