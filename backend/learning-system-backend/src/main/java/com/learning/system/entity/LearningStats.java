package com.learning.system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_stats")
public class LearningStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_date", nullable = false, unique = true)
    private LocalDate statDate;

    @Column(name = "total_study_time")
    private Integer totalStudyTime = 0; // 分钟

    @Column(name = "completed_tasks")
    private Integer completedTasks = 0;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    // 构造方法
    public LearningStats() {}

    public LearningStats(LocalDate statDate) {
        this.statDate = statDate;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getStatDate() { return statDate; }
    public void setStatDate(LocalDate statDate) { this.statDate = statDate; }

    public Integer getTotalStudyTime() { return totalStudyTime; }
    public void setTotalStudyTime(Integer totalStudyTime) { this.totalStudyTime = totalStudyTime; }

    public Integer getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Integer completedTasks) { this.completedTasks = completedTasks; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}