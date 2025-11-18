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
public class SubmissionRequestDTO {
    private String studentIndex;
    private Map<String, String> answers; // map from questionIndex -> answer text
}