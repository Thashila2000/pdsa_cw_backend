package com.hdse242052.task_scheduler.controller;

import com.hdse242052.task_scheduler.entity.Courses;
import com.hdse242052.task_scheduler.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("manageCourse/addCourse")
    public Courses addCourse(@RequestBody Courses course) {
        return courseService.addCourse(course);
    }


    @PutMapping("updateCourse")
    public Courses updateCourse(@RequestBody Courses course) {
        return courseService.updatecourse(course);
    }

    @DeleteMapping("delCourse/{id}")
    public  String deleteCourse(@PathVariable long id) {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/search")
    public Courses searchByName(@RequestParam String name) {
        return courseService.searchCourseByName(name);
    }




}
