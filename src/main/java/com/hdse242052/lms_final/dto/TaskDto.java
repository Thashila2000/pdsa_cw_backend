package com.hdse242052.lms_final.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private int duration;
    private LocalDateTime computedStart;
    private LocalDateTime computedEnd;
}