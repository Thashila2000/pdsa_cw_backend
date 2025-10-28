package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.CourseDto;
import com.hdse242052.lms_final.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CoursePublicController {
    private final CourseService service;

    public CoursePublicController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourseDto> getPublicCourses() {
        return service.getAllCourses(); // or filter published only
    }

    @GetMapping("/{id}")
    public CourseDto getCourseDetails(@PathVariable Long id) {
        return service.getCourseById(id);
    }
}