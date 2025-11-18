package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByDegreeId(Long degreeId);

    List<Subject> findByCategoryId(Long categoryId);

    @Query("SELECT s FROM Subject s WHERE s.degree.id = (SELECT u.degree.id FROM User u WHERE u.indexNumber = :index)")
    List<Subject> findSubjectsByStudentIndex(@Param("index") String index);

    @Query("SELECT s FROM Subject s WHERE s.degree IS NOT NULL")
    List<Subject> findSubjectsWithDegree();
}