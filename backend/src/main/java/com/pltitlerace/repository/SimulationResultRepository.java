package com.pltitlerace.repository;

import com.pltitlerace.model.SimulationResult;
import com.pltitlerace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResult, Long> {
    List<SimulationResult> findByGameweekOrderByTitleProbabilityDesc(Integer gameweek);
    Optional<SimulationResult> findByTeamAndGameweek(Team team, Integer gameweek);
    List<SimulationResult> findByTeamOrderByGameweekDesc(Team team);
}
