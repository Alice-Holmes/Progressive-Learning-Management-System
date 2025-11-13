package com.learning.system.repository;

import com.learning.system.entity.ReviewTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewTaskRepository extends JpaRepository<ReviewTask, Long> {
    List<ReviewTask> findByReviewDateAndStatus(LocalDate reviewDate, ReviewTask.TaskStatus status);

    @Query("SELECT rt FROM ReviewTask rt WHERE rt.reviewDate = :reviewDate")
    List<ReviewTask> findByReviewDate(LocalDate reviewDate);

    List<ReviewTask> findByStudyContentId(Long studyContentId);
}