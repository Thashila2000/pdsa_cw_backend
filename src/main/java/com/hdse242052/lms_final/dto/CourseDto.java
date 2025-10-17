package com.hdse242052.lms_final.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseDto {

    private Long id;
    private String title;
    private String description;
    private String level;
    private String imageUrl;
    private List<String> tags = new ArrayList<>();
    private List<String> prerequisites = new ArrayList<>();

    public CourseDto() {}

    public CourseDto(Long id, String title, String description, String level,
                     String imageUrl, List<String> tags, List<String> prerequisites) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.imageUrl = imageUrl;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.prerequisites = prerequisites != null ? prerequisites : new ArrayList<>();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites != null ? prerequisites : new ArrayList<>();
    }

    // Optional: equals, hashCode, toString for better debugging and comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDto)) return false;
        CourseDto that = (CourseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(level, that.level) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(prerequisites, that.prerequisites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, level, imageUrl, tags, prerequisites);
    }

    @Override
    public String toString() {
        return "CourseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", tags=" + tags +
                ", prerequisites=" + prerequisites +
                '}';
    }
}