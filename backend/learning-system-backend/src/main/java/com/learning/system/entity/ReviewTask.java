package com.learning.system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "review_task",schema = "plearningmsystem")
public class ReviewTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_content_id", nullable = false)
    private StudyContent studyContent;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    @Column(name = "review_stage", nullable = false)
    private Integer reviewStage;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    // 枚举定义
    public enum TaskStatus {
        PENDING, COMPLETED, SKIPPED
    }

    // 构造方法
    public ReviewTask() {}

    public ReviewTask(StudyContent studyContent, LocalDate reviewDate, Integer reviewStage) {
        this.studyContent = studyContent;
        this.reviewDate = reviewDate;
        this.reviewStage = reviewStage;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudyContent getStudyContent() { return studyContent; }
    public void setStudyContent(StudyContent studyContent) { this.studyContent = studyContent; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }

    public Integer getReviewStage() { return reviewStage; }
    public void setReviewStage(Integer reviewStage) { this.reviewStage = reviewStage; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCompletedTime() { return completedTime; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}