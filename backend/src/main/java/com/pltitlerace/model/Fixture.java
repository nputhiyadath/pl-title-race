package com.pltitlerace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fixtures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fixture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long externalId;

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Column(nullable = false)
    private Integer gameweek;

    @Enumerated(EnumType.STRING)
    private FixtureStatus status;

    private Integer homeScore;

    private Integer awayScore;

    public enum FixtureStatus {
        SCHEDULED,
        FINISHED,
        POSTPONED,
        CANCELLED
    }
}
