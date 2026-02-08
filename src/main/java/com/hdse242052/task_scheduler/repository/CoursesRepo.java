package com.hdse242052.task_scheduler.repository;

import com.hdse242052.task_scheduler.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CoursesRepo extends JpaRepository<Courses, Long> {
    List<Courses> id(Long id);
}
