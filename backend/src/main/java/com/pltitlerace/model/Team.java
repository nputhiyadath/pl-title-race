package com.pltitlerace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortName;

    private String crestUrl;

    @Column(nullable = false)
    private Double strength;

    private Integer position;

    private Integer playedGames;

    private Integer won;

    private Integer draw;

    private Integer lost;

    private Integer goalsFor;

    private Integer goalsAgainst;

    private Integer goalDifference;

    private Integer points;

    private String form;
}
