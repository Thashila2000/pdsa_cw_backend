package com.hdse242052.lms_final.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionResponseDTO {
    private Integer index;       // question order index (0-based)
    private String question;     // question text
    private String type;         // "short" or "mcq"
    private List<String> options; // MCQ options
    private String correctAnswer;
}