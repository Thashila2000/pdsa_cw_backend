package com.hdse242052.lms_final.dto;

public class DegreeResponse {
    private Long id;
    private String name;
    private Long categoryId;

    public DegreeResponse(Long id, String name, Long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public DegreeResponse(com.hdse242052.lms_final.entity.Degree degree) {
        this.id = degree.getId();
        this.name = degree.getName();
        this.categoryId = degree.getCategory() != null ? degree.getCategory().getId() : null;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getCategoryId() { return categoryId; }
}