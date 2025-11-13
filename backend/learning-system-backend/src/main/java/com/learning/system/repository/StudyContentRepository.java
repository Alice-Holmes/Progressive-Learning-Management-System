package com.learning.system.repository;

import com.learning.system.entity.StudyContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyContentRepository extends JpaRepository<StudyContent, Long> {
    List<StudyContent> findByStudyDate(LocalDate studyDate);
    List<StudyContent> findByCategory(String category);
}