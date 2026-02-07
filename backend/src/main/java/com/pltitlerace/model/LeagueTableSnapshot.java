package com.pltitlerace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "league_table_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueTableSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer gameweek;

    @Column(nullable = false)
    private Integer position;

    private Integer previousPosition;

    @Column(nullable = false)
    private Integer points;

    private Integer playedGames;

    private Integer won;

    private Integer draw;

    private Integer lost;

    private Integer goalsFor;

    private Integer goalsAgainst;

    private Integer goalDifference;

    private String form;

    @Column(nullable = false)
    private LocalDateTime snapshotDate;
}
