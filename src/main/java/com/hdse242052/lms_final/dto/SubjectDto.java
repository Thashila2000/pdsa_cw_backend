package com.hdse242052.lms_final.dto;

import com.hdse242052.lms_final.entity.Subject;

public class SubjectDto {
    private Long id;
    private String name;
    private String code;
    private String categoryName;
    private Long categoryId;
    private String degreeName;
    private Long degreeId;

    public SubjectDto(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.code = subject.getCode();

        if (subject.getCategory() != null) {
            this.categoryName = subject.getCategory().getName();
            this.categoryId = subject.getCategory().getId();
        }

        if (subject.getDegree() != null) {
            this.degreeName = subject.getDegree().getName();
            this.degreeId = subject.getDegree().getId();
        }
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }
}