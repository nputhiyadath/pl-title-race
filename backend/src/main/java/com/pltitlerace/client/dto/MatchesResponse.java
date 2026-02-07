package com.pltitlerace.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchesResponse {
    private Competition competition;
    private List<Match> matches;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Competition {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Match {
        private Long id;
        private LocalDateTime utcDate;
        private String status;
        private Integer matchday;
        private TeamInfo homeTeam;
        private TeamInfo awayTeam;
        private Score score;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamInfo {
        private Long id;
        private String name;
        private String shortName;
        private String tla;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Score {
        private String winner;
        private String duration;
        private ScoreDetail fullTime;
        private ScoreDetail halfTime;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScoreDetail {
        private Integer home;
        private Integer away;
    }
}
