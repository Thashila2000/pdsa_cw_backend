package com.hdse242052.lms_final.controller;
import com.hdse242052.lms_final.dto.SubjectDto;

import com.hdse242052.lms_final.dto.LoginRequest;
import com.hdse242052.lms_final.dto.RegisterRequest;
import com.hdse242052.lms_final.entity.Degree;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.entity.User;
import com.hdse242052.lms_final.repository.SubjectRepository;
import com.hdse242052.lms_final.repository.UserRepository;
import com.hdse242052.lms_final.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            String result = authService.register(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @Transactional
    @GetMapping("/subjects/{index}")
    public ResponseEntity<List<SubjectDto>> getSubjectsByIndex(@PathVariable String index) {
        Optional<User> userOpt = userRepository.findByIndexNumber(index);

        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        System.out.println("User found: " + user.getFullName() + ", degree: " +
                (user.getDegree() != null ? user.getDegree().getName() : "None"));

        Set<Subject> subjectSet = new HashSet<>();

        if (user.getDegree() != null) {
            Long degreeId = user.getDegree().getId();
            Long categoryId = user.getDegree().getCategory() != null
                    ? user.getDegree().getCategory().getId()
                    : null;

            List<Subject> degreeSubjects = subjectRepository.findByDegreeId(degreeId);
            System.out.println("Degree subjects found: " + degreeSubjects.size());
            subjectSet.addAll(degreeSubjects);

            if (categoryId != null) {
                List<Subject> categorySubjects = subjectRepository.findByCategoryId(categoryId);
                System.out.println("Category subjects found: " + categorySubjects.size());
                subjectSet.addAll(categorySubjects);
            }
        }

        List<SubjectDto> subjectDTOs = subjectSet.stream()
                .map(SubjectDto::new)
                .toList();

        System.out.println("Total subjects returned: " + subjectDTOs.size());
        return ResponseEntity.ok(subjectDTOs);
    }}