package com.pltitlerace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer gameweek;

    @Column(nullable = false)
    private Double titleProbability;

    private Double top4Probability;

    private Integer simulationRuns;

    private Double averageFinalPoints;

    private Integer minFinalPoints;

    private Integer maxFinalPoints;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;
}
