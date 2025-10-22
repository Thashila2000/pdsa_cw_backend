package com.hdse242052.lms_final.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private List<SubjectResponse> subjects;
}