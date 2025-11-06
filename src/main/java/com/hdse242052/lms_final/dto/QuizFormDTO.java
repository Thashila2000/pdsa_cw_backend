package com.hdse242052.lms_final.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizFormDTO {
    private String badgeSlug;
    private String formUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}