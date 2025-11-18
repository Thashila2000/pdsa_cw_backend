package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.*;
import com.hdse242052.lms_final.entity.OnlineQuizQuestion;
import com.hdse242052.lms_final.service.OnlineQuizService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/online-quizzes")
public class OnlineQuizController {

    private final OnlineQuizService quizService;
    private final ObjectMapper mapper = new ObjectMapper();

    public OnlineQuizController(OnlineQuizService quizService) {
        this.quizService = quizService;
    }

    // ------------------- CREATE QUIZ -------------------
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody CreateOnlineQuizDTO dto) {
        try {
            var saved = quizService.createQuiz(dto);
            return ResponseEntity.ok(Map.of(
                    "id", saved.getId(),
                    "message", "Quiz created successfully"
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    // ------------------- GET ACTIVE QUIZ FOR STUDENT -------------------
    @GetMapping("/student/{studentIndex}")
    public ResponseEntity<?> getQuizForStudent(@PathVariable String studentIndex) {
        var quizOpt = quizService.findActiveQuizForStudent(studentIndex);

        if (quizOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No active quiz found"));
        }

        var quizDTO = quizOpt.get();

        if (quizDTO.isSubmitted()) {
            // If already submitted, return only submitted=true, no answers
            return ResponseEntity.ok(Map.of(
                    "submitted", true,
                    "quizId", quizDTO.getId(),
                    "message", "You have already submitted this quiz"
            ));
        }

        // Map questions (already present in service DTO)
        return ResponseEntity.ok(Map.of(
                "submitted", false,
                "quiz", quizDTO
        ));
    }

    // ------------------- SUBMIT QUIZ -------------------
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody SubmissionRequestDTO req) {
        try {
            var result = quizService.submitAnswers(req);
            return ResponseEntity.ok(Map.of(
                    "message", "Quiz submitted successfully",
                    "result", result
            ));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(400).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    // ------------------- GET RESULT FOR STUDENT -------------------


}
