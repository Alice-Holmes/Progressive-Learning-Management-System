package com.learning.system.repository;

import com.learning.system.entity.LearningStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LearningStatsRepository extends JpaRepository<LearningStats, Long> {
    Optional<LearningStats> findByStatDate(LocalDate statDate);
}