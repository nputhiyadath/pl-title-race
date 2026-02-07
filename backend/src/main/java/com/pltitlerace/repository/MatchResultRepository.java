package com.pltitlerace.repository;

import com.pltitlerace.model.MatchResult;
import com.pltitlerace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    @Query("SELECT mr FROM MatchResult mr WHERE mr.fixture.homeTeam = :team OR mr.fixture.awayTeam = :team ORDER BY mr.recordedAt DESC")
    List<MatchResult> findByTeamOrderByRecordedAtDesc(Team team);
}
