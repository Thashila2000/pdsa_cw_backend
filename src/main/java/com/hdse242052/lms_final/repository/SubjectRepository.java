package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByCategory(Category category);
}