package com.learning.system.controller;

import com.learning.system.entity.ReviewTask;
import com.learning.system.entity.StudyContent;
import com.learning.system.service.StudyContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study")
//@CrossOrigin(origins = "http://localhost:8081") // 前端开发服务器地址
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:5173"})
public class StudyContentController {

    @Autowired
    private StudyContentService studyContentService;

    // 添加学习内容
    @PostMapping
    public ResponseEntity<StudyContent> addStudyContent(@RequestBody StudyContent content) {
        StudyContent saved = studyContentService.saveStudyContent(content);
        return ResponseEntity.ok(saved);
    }

    // 获取今日复习任务
    @GetMapping("/today-tasks")
    public ResponseEntity<List<ReviewTask>> getTodayTasks() {
        List<ReviewTask> tasks = studyContentService.getTodayReviewTasks();
        return ResponseEntity.ok(tasks);
    }

    // 完成任务
    @PutMapping("/task/{taskId}/complete")
    public ResponseEntity<ReviewTask> completeTask(@PathVariable Long taskId) {
        ReviewTask task = studyContentService.completeReviewTask(taskId);
        return ResponseEntity.ok(task);
    }

//    // 获取所有学习内容（用于测试）
//    @GetMapping
//    public ResponseEntity<List<StudyContent>> getAllStudyContents() {
//        List<StudyContent> contents = studyContentService.getAllStudyContents();
//        return ResponseEntity.ok(contents);
//    }

    // 获取所有学习内容
    @GetMapping("/all")
    public ResponseEntity<List<StudyContent>> getAllStudyContents() {
        List<StudyContent> contents = studyContentService.getAllStudyContents();
        return ResponseEntity.ok(contents);
    }

    // 删除学习内容
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteStudyContent(@PathVariable Long contentId) {
        studyContentService.deleteStudyContent(contentId);
        return ResponseEntity.ok().build();
    }

    // 根据ID获取学习内容
    @GetMapping("/{contentId}")
    public ResponseEntity<StudyContent> getStudyContentById(@PathVariable Long contentId) {
        StudyContent content = studyContentService.getStudyContentById(contentId);
        return ResponseEntity.ok(content);
    }

    // 获取今日已完成任务
    @GetMapping("/today-completed-tasks")
    public ResponseEntity<List<ReviewTask>> getTodayCompletedTasks() {
        List<ReviewTask> tasks = studyContentService.getTodayCompletedTasks();
        return ResponseEntity.ok(tasks);
    }

    // 获取今日待完成任务
    @GetMapping("/today-pending-tasks")
    public ResponseEntity<List<ReviewTask>> getTodayPendingTasks() {
        List<ReviewTask> tasks = studyContentService.getTodayPendingTasks();
        return ResponseEntity.ok(tasks);
    }
}