package com.learning.system.service;

import com.learning.system.entity.ReviewTask;
import com.learning.system.entity.StudyContent;
import com.learning.system.repository.ReviewTaskRepository;
import com.learning.system.repository.StudyContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

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

//    public List<StudyContent> getAllStudyContents() {
//        return studyContentRepository.findAll();
//    }

    // 删除学习内容及相关复习任务
    @Transactional
    public void deleteStudyContent(Long contentId) {
        // 先删除相关的复习任务
        List<ReviewTask> tasks = reviewTaskRepository.findByStudyContentId(contentId);
        reviewTaskRepository.deleteAll(tasks);

        // 再删除学习内容
        studyContentRepository.deleteById(contentId);
    }

    // 获取所有学习内容（按时间倒序）
    public List<StudyContent> getAllStudyContents() {
        return studyContentRepository.findAll(Sort.by(Sort.Direction.DESC, "studyDate"));
    }

    // 获取今日已完成任务
    public List<ReviewTask> getTodayCompletedTasks() {
        return reviewTaskRepository.findByReviewDateAndStatus(LocalDate.now(), ReviewTask.TaskStatus.COMPLETED);
    }

    // 获取今日待完成任务
    public List<ReviewTask> getTodayPendingTasks() {
        return reviewTaskRepository.findByReviewDateAndStatus(LocalDate.now(), ReviewTask.TaskStatus.PENDING);
    }

    // 获取所有已完成任务
    public List<ReviewTask> getAllCompletedTasks() {
        return reviewTaskRepository.findByStatus(ReviewTask.TaskStatus.COMPLETED);
    }

    // 根据ID获取学习内容
    public StudyContent getStudyContentById(Long id) {
        return studyContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学习内容不存在: " + id));
    }
}