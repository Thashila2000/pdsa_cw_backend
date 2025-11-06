package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.QuizFormDTO;
import com.hdse242052.lms_final.entity.QuizForm;
import com.hdse242052.lms_final.entity.User;
import com.hdse242052.lms_final.service.QuizFormService;
import com.hdse242052.lms_final.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizFormController {

    @Autowired
    private QuizFormService quizService;

    @Autowired
    private UserRepository userRepo;

    // Add a new quiz
    @PostMapping
    public ResponseEntity<?> addQuiz(@RequestBody QuizFormDTO dto) {
        if (dto.getBadgeSlug() == null || dto.getBadgeSlug().isBlank()) {
            return ResponseEntity.badRequest().body("badgeSlug is required");
        }

        QuizForm saved = quizService.saveQuiz(dto);
        return ResponseEntity.ok(saved);
    }

    // Get active quiz by student index number
    @GetMapping
    public ResponseEntity<?> getQuizByIndex(@RequestParam String indexNumber) {
        Optional<User> optionalUser = userRepo.findByIndexNumber(indexNumber);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ User not found for index: " + indexNumber);
        }

        User user = optionalUser.get();
        String badgeSlug = user.getDegreeName().trim();

        try {
            QuizForm quiz = quizService.getActiveQuizByBadge(badgeSlug);
            return ResponseEntity.ok(quiz);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("⏳ " + e.getMessage());
        }
    }

    // Get all quizzes by badgeSlug (for admin view)
    @GetMapping("/by-badge/{badgeSlug}")
    public ResponseEntity<?> getQuizzesByBadge(@PathVariable String badgeSlug) {
        List<QuizForm> quizzes = quizService.getAllQuizzesByBadge(badgeSlug.trim());

        if (quizzes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("⚠️ No quizzes found for badge: " + badgeSlug);
        }

        return ResponseEntity.ok(quizzes);
    }
}