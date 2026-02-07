package com.pltitlerace.repository;

import com.pltitlerace.model.Fixture;
import com.pltitlerace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    Optional<Fixture> findByExternalId(Long externalId);
    List<Fixture> findByGameweek(Integer gameweek);
    List<Fixture> findByStatus(Fixture.FixtureStatus status);

    @Query("SELECT f FROM Fixture f WHERE f.status = 'SCHEDULED' AND (f.homeTeam = :team OR f.awayTeam = :team) ORDER BY f.matchDate ASC")
    List<Fixture> findRemainingFixturesByTeam(Team team);

    @Query("SELECT f FROM Fixture f WHERE f.homeTeam = :team OR f.awayTeam = :team ORDER BY f.matchDate DESC")
    List<Fixture> findByTeamOrderByMatchDateDesc(Team team);
}
