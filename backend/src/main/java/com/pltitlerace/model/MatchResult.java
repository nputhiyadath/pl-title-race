package com.pltitlerace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "fixture_id", unique = true, nullable = false)
    private Fixture fixture;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Team winner;

    @Column(nullable = false)
    private Integer homeScore;

    @Column(nullable = false)
    private Integer awayScore;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    private LocalDateTime recordedAt;

    public enum ResultType {
        HOME_WIN,
        AWAY_WIN,
        DRAW
    }
}
