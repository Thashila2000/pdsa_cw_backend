package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.DegreeRequest;
import com.hdse242052.lms_final.dto.DegreeResponse;
import com.hdse242052.lms_final.entity.Degree;
import com.hdse242052.lms_final.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degrees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DegreeController {
    private final DegreeService degreeService;

    @PostMapping("/add")
    public ResponseEntity<String> addDegree(@RequestBody DegreeRequest request) {
        degreeService.addDegree(request);
        return ResponseEntity.ok("Degree and subjects added successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<DegreeResponse>> getAllDegrees() {
        List<Degree> degrees = degreeService.getAllDegrees();
        List<DegreeResponse> response = degrees.stream()
                .map(DegreeResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

}