package com.pltitlerace.controller;

import com.pltitlerace.dto.RunInDifficultyDto;
import com.pltitlerace.service.RunInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/run-in")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RunInController {

    private final RunInService runInService;

    @GetMapping("/difficulty")
    public ResponseEntity<List<RunInDifficultyDto>> getRunInDifficulty() {
        return ResponseEntity.ok(runInService.calculateRunInDifficulty());
    }

    @GetMapping("/difficulty/{teamId}")
    public ResponseEntity<RunInDifficultyDto> getTeamRunInDifficulty(@PathVariable Long teamId) {
        return ResponseEntity.ok(runInService.calculateTeamRunInDifficulty(teamId));
    }
}
