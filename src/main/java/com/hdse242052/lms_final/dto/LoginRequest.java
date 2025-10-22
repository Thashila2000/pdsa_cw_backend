package com.hdse242052.lms_final.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String indexNumber;
    private String password;
}