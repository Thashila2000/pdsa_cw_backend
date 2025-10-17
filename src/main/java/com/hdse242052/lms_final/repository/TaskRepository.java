package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}