package com.hdse242052.lms_final.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private int duration; // in hours

    private int priority; // lower number = higher priority

    private LocalDateTime startTime;

    @ElementCollection
    @CollectionTable(
            name = "task_dependencies",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "dependency_id")
    private List<Long> dependencies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id", nullable = false)
    @JsonBackReference(value = "degree-task")
    private Degree degree;
}