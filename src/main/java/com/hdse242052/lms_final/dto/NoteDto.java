package com.hdse242052.lms_final.dto;

import lombok.Data;

@Data
public class NoteDto {
    private Long id;
    private String text;
    private String imageUri;
    private Long timestamp;
    private Long subjectId;
}