package com.hdse242052.lms_final.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badgeSlug;

    private String formUrl;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}