package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.SubjectDto;
import com.hdse242052.lms_final.dto.LoginRequest;
import com.hdse242052.lms_final.dto.RegisterRequest;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // Register
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

    // Login
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

    //  Get Degree ID by Index
    @GetMapping("/degree")
    public ResponseEntity<Long> getDegreeIdByIndex(@RequestParam String indexNumber) {
        Optional<User> userOpt = userRepository.findByIndexNumber(indexNumber);
        if (userOpt.isPresent() && userOpt.get().getDegree() != null) {
            return ResponseEntity.ok(userOpt.get().getDegree().getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //  Fetch Profile
    @GetMapping("/users/profile")
    public ResponseEntity<?> getProfile(@RequestParam String indexNumber) {
        User user = userRepository.findByIndexNumber(indexNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, String> profile = new HashMap<>();
        profile.put("fullName", user.getFullName());
        profile.put("indexNumber", user.getIndexNumber());
        profile.put("degreeName", user.getDegreeName());
        profile.put("profileImageUrl", user.getProfileImageUrl());

        return ResponseEntity.ok(profile);
    }

    // Upload Profile Image
    @PostMapping("/users/{indexNumber}/upload-profile")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @PathVariable String indexNumber,
            @RequestParam("image") MultipartFile file) throws IOException {

        User user = userRepository.findByIndexNumber(indexNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String filename = indexNumber + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/profile/" + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        user.setProfileImageUrl("/uploads/profile/" + filename);
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile image uploaded");
        response.put("profileImageUrl", user.getProfileImageUrl());

        return ResponseEntity.ok(response);
    }


    // Fetch Subjects by Index
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