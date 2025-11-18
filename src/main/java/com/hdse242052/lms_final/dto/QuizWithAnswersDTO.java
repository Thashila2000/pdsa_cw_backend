package com.hdse242052.lms_final.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuizWithAnswersDTO {
    private Long id;
    private String badgeSlug;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuestionWithAnswerDTO> questions;

    // New fields to match frontend expectations
    private boolean submitted; // if the student has submitted
    private SubmissionResultDTO previousResult;
}
