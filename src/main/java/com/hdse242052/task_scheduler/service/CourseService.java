package com.hdse242052.task_scheduler.service;

import com.hdse242052.task_scheduler.entity.Courses;
import com.hdse242052.task_scheduler.repository.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CoursesRepo coursesRepo;

    public Courses addCourse(Courses course) {
        return coursesRepo.save(course);
    }

    public Courses updatecourse(Courses course) {

        Courses existing = coursesRepo.findById(course.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + course.getId()));
        existing.setName(course.getName());
        existing.setDescription(course.getDescription());
        return coursesRepo.save(existing);

    }

    public String deleteCourse(long id) {
         coursesRepo.deleteById(id);
        return id+ "Product deleted";
    }




    public Courses searchCourseByName(String name) {
        List<Courses> courses = coursesRepo.findAll();
        courses.sort(Comparator.comparing(c -> c.getName().toLowerCase()));

        int low = 0;
        int high = courses.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Courses midCourse = courses.get(mid);

            int comparison = midCourse.getName().compareToIgnoreCase(name);

            if (comparison == 0)
                return midCourse; // Found
            else if (comparison < 0)
                low = mid + 1; // Search right half
            else
                high = mid - 1; // Search left half
        }

        throw new RuntimeException("Course not found with name: " + name);
    }
}
