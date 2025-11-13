package com.learning.system.service;

import com.learning.system.entity.ReviewTask;
import com.learning.system.entity.StudyContent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReviewScheduler {

    // 基于遗忘曲线的复习间隔（天）
    private static final int[] REVIEW_INTERVALS = {1, 2, 4, 7, 15, 30};

    public List<LocalDate> generateReviewDates(LocalDate studyDate) {
        return Arrays.stream(REVIEW_INTERVALS)
                .mapToObj(interval -> studyDate.plusDays(interval)) // 遍历间隔，计算每个复习日期
                .collect(Collectors.toList()); // 收集为List<LocalDate>
    }

    public List<ReviewTask> createReviewTasks(StudyContent content) {
        List<LocalDate> reviewDates = generateReviewDates(content.getStudyDate());
        List<ReviewTask> tasks = new ArrayList<>();

        for (int i = 0; i < reviewDates.size(); i++) {
            ReviewTask task = new ReviewTask();
            task.setStudyContent(content);
            task.setReviewDate(reviewDates.get(i));
            task.setReviewStage(i + 1);
            tasks.add(task);
        }

        return tasks;
    }
}