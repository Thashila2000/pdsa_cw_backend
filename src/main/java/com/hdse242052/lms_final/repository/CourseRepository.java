package com.hdse242052.lms_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hdse242052.lms_final.entity.CourseEntity;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findByTitleContainingIgnoreCase(String keyword);
}