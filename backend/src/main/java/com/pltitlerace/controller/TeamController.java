package com.pltitlerace.controller;

import com.pltitlerace.dto.TeamFormDto;
import com.pltitlerace.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{id}/form")
    public ResponseEntity<TeamFormDto> getTeamForm(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamForm(id));
    }
}
