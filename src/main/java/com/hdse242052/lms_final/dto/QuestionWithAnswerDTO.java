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
public class QuestionWithAnswerDTO {
    private String question;
    private String type;
    private List<String> options;
    private String correctAnswer;
}
