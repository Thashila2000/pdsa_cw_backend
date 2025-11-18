package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.SubjectDto;
import com.hdse242052.lms_final.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/by-student/{indexNumber}")
    public List<SubjectDto> getSubjectsForStudent(@PathVariable String indexNumber) {
        return subjectService.getSubjectsByStudentIndex(indexNumber);
    }
}