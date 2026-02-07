package com.pltitlerace.controller;

import com.pltitlerace.dto.TeamStandingDto;
import com.pltitlerace.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;

    @GetMapping("/current")
    public ResponseEntity<List<TeamStandingDto>> getCurrentTable() {
        return ResponseEntity.ok(tableService.getCurrentTable());
    }

    @GetMapping("/gameweek/{gameweek}")
    public ResponseEntity<List<TeamStandingDto>> getTableByGameweek(@PathVariable Integer gameweek) {
        return ResponseEntity.ok(tableService.getTableByGameweek(gameweek));
    }
}
