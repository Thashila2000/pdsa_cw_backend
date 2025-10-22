package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.ResultRequest;
import com.hdse242052.lms_final.entity.Result;
import com.hdse242052.lms_final.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/add")
    public ResponseEntity<Result> addResult(@RequestBody ResultRequest request) {
        Result result = Result.builder()
                .indexNumber(request.getIndexNumber())
                .subjectName(request.getSubjectName())
                .courseworkGrade(request.getCourseworkGrade())
                .examGrade(request.getExamGrade())
                .gpa(request.getGpa())
                .build();

        return ResponseEntity.ok(resultService.saveResult(result));
    }

    @GetMapping("/{indexNumber}")
    public ResponseEntity<List<Result>> getResults(@PathVariable String indexNumber) {
        List<Result> results = resultService.getResultsByIndex(indexNumber);
        return ResponseEntity.ok(results);
    }
}