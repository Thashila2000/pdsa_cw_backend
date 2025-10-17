package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}