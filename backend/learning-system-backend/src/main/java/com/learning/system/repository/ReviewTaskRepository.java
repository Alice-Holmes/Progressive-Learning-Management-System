package com.learning.system.repository;

import com.learning.system.entity.ReviewTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewTaskRepository extends JpaRepository<ReviewTask, Long> {
    // 按日期和状态查询任务
    List<ReviewTask> findByReviewDateAndStatus(LocalDate reviewDate, ReviewTask.TaskStatus status);

    // 按状态查询所有任务
    List<ReviewTask> findByStatus(ReviewTask.TaskStatus status);

    @Query("SELECT rt FROM ReviewTask rt WHERE rt.reviewDate = :reviewDate")
    List<ReviewTask> findByReviewDate(LocalDate reviewDate);

    List<ReviewTask> findByStudyContentId(Long studyContentId);
}