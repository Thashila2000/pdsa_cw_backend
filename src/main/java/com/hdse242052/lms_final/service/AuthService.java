package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.LoginRequest;
import com.hdse242052.lms_final.dto.RegisterRequest;
import com.hdse242052.lms_final.entity.User;
import com.hdse242052.lms_final.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        if (userRepository.existsByIndexNumber(request.getIndexNumber())) {
            return "Index number already exists.";
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .indexNumber(request.getIndexNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : "STUDENT")
                .degreeName(request.getDegreeName())
                .build();

        userRepository.save(user);
        return "Student registered successfully.";
    }

    public ResponseEntity<?> login(LoginRequest request) {
        return userRepository.findByIndexNumber(request.getIndexNumber())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Login successful.");
                    response.put("indexNumber", user.getIndexNumber());
                    response.put("name", user.getFullName()); // using fullName from your entity
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid index number or password.")));
    }

}