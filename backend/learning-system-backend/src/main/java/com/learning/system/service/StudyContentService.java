package com.learning.system.service;

import com.learning.system.entity.ReviewTask;
import com.learning.system.entity.StudyContent;
import com.learning.system.repository.ReviewTaskRepository;
import com.learning.system.repository.StudyContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudyContentService {

    @Autowired
    private StudyContentRepository studyContentRepository;

    @Autowired
    private ReviewTaskRepository reviewTaskRepository;

    @Autowired
    private ReviewScheduler reviewScheduler;

    @Transactional
    public StudyContent saveStudyContent(StudyContent content) {
        // 确保学习日期不为空
        if (content.getStudyDate() == null) {
            content.setStudyDate(LocalDate.now());
        }

        StudyContent savedContent = studyContentRepository.save(content);

        // 生成复习任务
        List<ReviewTask> reviewTasks = reviewScheduler.createReviewTasks(savedContent);
        reviewTaskRepository.saveAll(reviewTasks);

        return savedContent;
    }

    public List<ReviewTask> getTodayReviewTasks() {
        return reviewTaskRepository.findByReviewDate(LocalDate.now());
    }

    @Transactional
    public ReviewTask completeReviewTask(Long taskId) {
        ReviewTask task = reviewTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在: " + taskId));

        task.setStatus(ReviewTask.TaskStatus.COMPLETED);
        task.setCompletedTime(java.time.LocalDateTime.now());

        return reviewTaskRepository.save(task);
    }

    public List<StudyContent> getAllStudyContents() {
        return studyContentRepository.findAll();
    }
}