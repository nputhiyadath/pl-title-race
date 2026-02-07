package com.pltitlerace.client;

import com.pltitlerace.client.dto.MatchesResponse;
import com.pltitlerace.client.dto.StandingsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class FootballDataClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${football.api.base-url}")
    private String baseUrl;

    @Value("${football.api.api-key}")
    private String apiKey;

    private static final String PREMIER_LEAGUE_CODE = "PL";

    public StandingsResponse getStandings() {
        try {
            String url = baseUrl + "/competitions/" + PREMIER_LEAGUE_CODE + "/standings";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", apiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<StandingsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                StandingsResponse.class
            );

            log.info("Successfully fetched standings from football-data.org API");
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching standings from football-data.org API: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch standings from API", e);
        }
    }

    public MatchesResponse getMatches(String status) {
        try {
            String url = baseUrl + "/competitions/" + PREMIER_LEAGUE_CODE + "/matches?status=" + status;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", apiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<MatchesResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MatchesResponse.class
            );

            log.info("Successfully fetched {} matches from football-data.org API", status);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching matches from football-data.org API: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch matches from API", e);
        }
    }
}
