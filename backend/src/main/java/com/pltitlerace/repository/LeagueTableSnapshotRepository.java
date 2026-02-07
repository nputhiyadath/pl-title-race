package com.pltitlerace.repository;

import com.pltitlerace.model.LeagueTableSnapshot;
import com.pltitlerace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueTableSnapshotRepository extends JpaRepository<LeagueTableSnapshot, Long> {
    List<LeagueTableSnapshot> findByGameweekOrderByPositionAsc(Integer gameweek);
    Optional<LeagueTableSnapshot> findByTeamAndGameweek(Team team, Integer gameweek);
    List<LeagueTableSnapshot> findByTeamOrderByGameweekDesc(Team team);
}
