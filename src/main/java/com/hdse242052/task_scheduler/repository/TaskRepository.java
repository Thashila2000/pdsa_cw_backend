package com.hdse242052.task_scheduler.repository;

import com.hdse242052.task_scheduler.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}