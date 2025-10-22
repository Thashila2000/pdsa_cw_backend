package com.hdse242052.lms_final.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String indexNumber;
    private String email;
    private String password;
    private String role;
    private String degreeName;
}