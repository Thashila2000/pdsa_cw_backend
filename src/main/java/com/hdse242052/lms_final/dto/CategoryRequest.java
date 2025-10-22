package com.hdse242052.lms_final.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryRequest {
    private String name;
    private List<SubjectRequest> subjects;
}