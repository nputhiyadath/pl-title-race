package com.pltitlerace.controller;

import com.pltitlerace.dto.SimulationRequestDto;
import com.pltitlerace.dto.TitleRaceProjectionDto;
import com.pltitlerace.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/title-race")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TitleRaceController {

    private final SimulationService simulationService;

    @GetMapping("/projection")
    public ResponseEntity<TitleRaceProjectionDto> getTitleRaceProjection() {
        return ResponseEntity.ok(simulationService.getCurrentProjection());
    }

    @PostMapping("/simulate")
    public ResponseEntity<TitleRaceProjectionDto> runSimulation(
            @RequestParam(defaultValue = "10000") Integer runs) {
        return ResponseEntity.ok(simulationService.runSimulation(runs));
    }

    @PostMapping("/simulate-constrained")
    public ResponseEntity<TitleRaceProjectionDto> runConstrainedSimulation(
            @RequestBody SimulationRequestDto request) {

        Map<Long, String> constrainedResults = new HashMap<>();
        if (request.getConstrainedFixtures() != null) {
            request.getConstrainedFixtures().forEach(fixture ->
                constrainedResults.put(fixture.getFixtureId(), fixture.getResult())
            );
        }

        Integer runs = request.getRuns() != null ? request.getRuns() : 10000;

        return ResponseEntity.ok(simulationService.runConstrainedSimulation(runs, constrainedResults));
    }
}
