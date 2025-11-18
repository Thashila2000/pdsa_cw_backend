package com.hdse242052.lms_final.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private int duration;
    private int priority;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime computedStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime computedEnd;

    // Degree info for frontend filtering
    private Long degreeId;
    private String degreeName;

    // Optional: include dependencies if needed
    private List<Long> dependencies;
}