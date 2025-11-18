package com.hdse242052.lms_final.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SubmissionResultDTO {
    private int score;
    private int total;
    private Map<Integer, String> studentAnswers;
    private QuizWithAnswersDTO quiz;

    // Optional constructor for convenience
    public SubmissionResultDTO(Integer score, Integer total, Map<Integer, String> studentAnswers, QuizWithAnswersDTO quiz) {
        this.score = score;
        this.total = total;
        this.studentAnswers = studentAnswers;
        this.quiz = quiz;
    }
}
