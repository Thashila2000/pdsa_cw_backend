package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {



    // Subjects directly linked to a degree
    List<Subject> findByDegreeId(Long degreeId);

    //  subjects linked by category ID (for inherited subjects)
    List<Subject> findByCategoryId(Long categoryId);
}
