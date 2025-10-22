package com.hdse242052.lms_final.dto;

import lombok.Data;

@Data
public class ResultRequest {
    private String indexNumber;
    private String subjectName;
    private String courseworkGrade;
    private String examGrade;
    private double gpa;
}