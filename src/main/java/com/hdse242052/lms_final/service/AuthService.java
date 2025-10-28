package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.LoginRequest;
import com.hdse242052.lms_final.dto.RegisterRequest;
import com.hdse242052.lms_final.entity.Degree;
import com.hdse242052.lms_final.entity.User;
import com.hdse242052.lms_final.repository.DegreeRepository;
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
    private final DegreeRepository degreeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new student user and assign the Degree entity based on degreeName.
     */
    public String register(RegisterRequest request) {
        // Check if index number already exists
        if (userRepository.existsByIndexNumber(request.getIndexNumber())) {
            return "Index number already exists.";
        }

        // Find the Degree entity by name
        Degree degree = degreeRepository.findByName(request.getDegreeName())
                .orElseThrow(() -> new RuntimeException(
                        "Degree not found: " + request.getDegreeName()
                ));

        // Build and save the User entity
        User user = User.builder()
                .fullName(request.getFullName())
                .indexNumber(request.getIndexNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : "STUDENT")
                .degree(degree)                  // ✅ assign actual Degree entity
                .degreeName(degree.getName())    // optional convenience field
                .build();

        userRepository.save(user);
        return "Student registered successfully.";
    }

    /**
     * Login a student user by index number and password.
     */
    public ResponseEntity<?> login(LoginRequest request) {
        return userRepository.findByIndexNumber(request.getIndexNumber())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Login successful.");
                    response.put("indexNumber", user.getIndexNumber());
                    response.put("name", user.getFullName());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid index number or password.")));
    }

}
