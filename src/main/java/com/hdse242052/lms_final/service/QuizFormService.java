package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.QuizFormDTO;
import com.hdse242052.lms_final.entity.QuizForm;
import com.hdse242052.lms_final.repository.QuizFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizFormService {

    @Autowired
    private QuizFormRepository quizRepo;

    // Save a new quiz
    public QuizForm saveQuiz(QuizFormDTO dto) {
        QuizForm quiz = QuizForm.builder()
                .badgeSlug(dto.getBadgeSlug().trim().toLowerCase()) // normalize slug
                .formUrl(dto.getFormUrl())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
        return quizRepo.save(quiz);
    }

    //  Get first quiz by badgeSlug (case-insensitive)
    public QuizForm getQuizByBadge(String badgeSlug) {
        return quizRepo.findByBadgeSlugIgnoreCase(badgeSlug.trim())
                .orElseThrow(() -> new RuntimeException("Quiz not found for badge: " + badgeSlug));
    }

    // Get all quizzes for a badge (case-insensitive)
    public List<QuizForm> getAllQuizzesByBadge(String badgeSlug) {
        return quizRepo.findAllByBadgeSlugIgnoreCase(badgeSlug.trim());
    }

    // Get first active quiz for a badge
    public QuizForm getActiveQuizByBadge(String badgeSlug) {
        List<QuizForm> quizzes = quizRepo.findAllByBadgeSlugIgnoreCase(badgeSlug.trim());

        return quizzes.stream()
                .filter(this::isQuizActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active quiz for badge: " + badgeSlug));
    }

    //  Check if quiz is currently active
    public boolean isQuizActive(QuizForm quiz) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("🕒 Now: " + now);
        System.out.println("🟢 Start: " + quiz.getStartTime());
        System.out.println("🔴 End: " + quiz.getEndTime());

        return !now.isBefore(quiz.getStartTime()) && !now.isAfter(quiz.getEndTime());
    }
}