package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.CourseDto;
import com.hdse242052.lms_final.service.CourseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    /**
     * Returns all courses.
     */
    @GetMapping
    public List<CourseDto> getAllCourses() {
        return service.getAllCourses();
    }

    /**
     * Returns a course by ID.
     */
    @GetMapping("/{id}")
    public CourseDto getCourseById(@PathVariable Long id) {
        return service.getCourseById(id);
    }

    /**
     * Creates a new course.
     */
    @PostMapping
    public CourseDto createCourse(@RequestBody CourseDto dto) {
        return service.saveCourse(dto);
    }

    /**
     * Updates an existing course.
     */
    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @RequestBody CourseDto dto) {
        return service.updateCourse(id, dto);
    }

    /**
     * Deletes a course by ID.
     */
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        service.deleteCourse(id);
    }

    /**
     * Searches for courses by title keyword.
     */
    @GetMapping("/search")
    public List<CourseDto> searchCourses(@RequestParam("q") String query) {
        return service.searchCourses(query);
    }

    /**
     * Handles image upload and returns a public image URL.
     */
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads", filename);
        Files.createDirectories(uploadPath.getParent());
        Files.write(uploadPath, file.getBytes());

        // Return relative path to be used by frontend
        return "/uploads/" + filename;
    }
}