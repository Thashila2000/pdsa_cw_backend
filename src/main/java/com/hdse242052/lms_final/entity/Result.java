package com.hdse242052.lms_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String indexNumber;
    private String subjectName;

    private String courseworkGrade; // e.g. A, B, C
    private String examGrade;       // e.g. A, B, C

    private double gpa;


}