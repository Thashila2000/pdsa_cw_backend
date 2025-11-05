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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

    @GetMapping("/degree")
    public ResponseEntity<Long> getDegreeIdByIndex(@RequestParam String indexNumber) {
        Optional<User> userOpt = userRepository.findByIndexNumber(indexNumber);
        if (userOpt.isPresent() && userOpt.get().getDegree() != null) {
            return ResponseEntity.ok(userOpt.get().getDegree().getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByIndexNumber(request.getIndexNumber());

        if (userOpt.isEmpty()) {
            System.out.println("Login failed: user not found for index " + request.getIndexNumber());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Login failed: incorrect password for index " + request.getIndexNumber());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        System.out.println("Login successful for user: " + user.getFullName());
        return ResponseEntity.ok(user);
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
    }
}