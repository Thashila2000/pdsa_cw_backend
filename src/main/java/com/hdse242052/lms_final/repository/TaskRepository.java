package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // Fetch all tasks for a specific degree
    List<TaskEntity> findByDegreeId(Long degreeId);

}