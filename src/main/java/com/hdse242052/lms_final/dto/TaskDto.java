package com.hdse242052.lms_final.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private int duration;
    private int priority;

    private LocalDateTime computedStart;
    private LocalDateTime computedEnd;

    // Degree info for frontend filtering
    private Long degreeId;
    private String degreeName;



    // Optional: include dependencies if needed
    private List<Long> dependencies;
}