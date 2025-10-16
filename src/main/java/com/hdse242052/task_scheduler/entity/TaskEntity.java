package com.hdse242052.task_scheduler.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration;     // in minutes or hours — clarify in UI
    private int priority;     // lower number = higher priority
    private LocalDateTime startTime;

    @ElementCollection
    @CollectionTable(
            name = "task_dependencies",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "dependency_id")
    private List<Long> dependencies;
}