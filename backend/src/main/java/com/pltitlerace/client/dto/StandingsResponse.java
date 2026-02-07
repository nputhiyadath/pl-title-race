package com.pltitlerace.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingsResponse {
    private Competition competition;
    private List<Standing> standings;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Competition {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Standing {
        private String type;
        private List<TableEntry> table;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TableEntry {
        private Integer position;
        private TeamInfo team;
        private Integer playedGames;
        private String form;
        private Integer won;
        private Integer draw;
        private Integer lost;
        private Integer points;
        private Integer goalsFor;
        private Integer goalsAgainst;
        private Integer goalDifference;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamInfo {
        private Long id;
        private String name;
        private String shortName;
        private String tla;
        private String crest;
    }
}
