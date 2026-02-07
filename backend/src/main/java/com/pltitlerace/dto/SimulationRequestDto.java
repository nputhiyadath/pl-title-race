package com.pltitlerace.dto;

import lombok.Data;
import java.util.List;

@Data
public class SimulationRequestDto {
    private Integer runs = 10000;
    private List<FixtureResultDto> constrainedFixtures;
}
