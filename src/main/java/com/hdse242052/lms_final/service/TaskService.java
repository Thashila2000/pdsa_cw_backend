package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.entity.TaskEntity;
import com.hdse242052.lms_final.dto.TaskDto;
import com.hdse242052.lms_final.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // Converts TaskEntity to TaskDto with full metadata
    private TaskDto toDto(TaskEntity task, LocalDateTime computedStart) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDuration(task.getDuration());
        dto.setPriority(task.getPriority());
        dto.setDependencies(task.getDependencies());
        dto.setComputedStart(computedStart);
        dto.setComputedEnd(computedStart.plusHours(task.getDuration()));

        if (task.getDegree() != null) {
            dto.setDegreeId(task.getDegree().getId());
            dto.setDegreeName(task.getDegree().getName());
        }

        return dto;
    }

    // Computes full schedule with dependency-aware start and end times
    public List<TaskDto> computeSchedule(List<TaskEntity> tasks) {
        Map<Long, TaskEntity> taskMap = tasks.stream()
                .collect(Collectors.toMap(TaskEntity::getId, t -> t));

        Map<Long, LocalDateTime> startTimes = new HashMap<>();
        for (TaskEntity task : tasks) {
            startTimes.put(task.getId(), LocalDateTime.MAX);
        }

        for (TaskEntity task : tasks) {
            if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
                startTimes.put(task.getId(), task.getStartTime());
            }
        }

        Set<Long> visited = new HashSet<>();
        for (TaskEntity task : tasks) {
            visit(task.getId(), taskMap, startTimes, visited);
        }

        List<TaskDto> result = new ArrayList<>();
        for (TaskEntity task : tasks) {
            LocalDateTime start = startTimes.get(task.getId());
            result.add(toDto(task, start));
        }

        return result;
    }

    // Recursive DFS to compute start time based on dependencies
    private LocalDateTime visit(Long id, Map<Long, TaskEntity> taskMap,
                                Map<Long, LocalDateTime> startTimes, Set<Long> visited) {
        if (visited.contains(id)) return startTimes.get(id);
        visited.add(id);

        TaskEntity task = taskMap.get(id);
        LocalDateTime maxEnd = task.getStartTime() != null ? task.getStartTime() : LocalDateTime.MIN;

        for (Long depId : task.getDependencies()) {
            TaskEntity dep = taskMap.get(depId);
            if (dep != null) {
                LocalDateTime depEnd = visit(depId, taskMap, startTimes, visited)
                        .plusHours(dep.getDuration());
                if (depEnd.isAfter(maxEnd)) {
                    maxEnd = depEnd;
                }
            }
        }

        startTimes.put(id, maxEnd);
        return maxEnd;
    }

    // Computes correct start time for a task based on its dependencies
    public LocalDateTime computeStartTimeFromDependencies(TaskEntity task, List<TaskEntity> allTasks) {
        if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
            return task.getStartTime(); // No dependencies, use provided start time
        }

        Map<Long, TaskEntity> taskMap = allTasks.stream()
                .collect(Collectors.toMap(TaskEntity::getId, t -> t));

        LocalDateTime latestEnd = LocalDateTime.MIN;

        for (Long depId : task.getDependencies()) {
            TaskEntity dep = taskMap.get(depId);
            if (dep != null && dep.getStartTime() != null) {
                // Optional: restrict to same degree
                if (task.getDegree() != null && dep.getDegree() != null &&
                        !task.getDegree().getId().equals(dep.getDegree().getId())) {
                    continue; // skip cross-degree dependencies
                }

                LocalDateTime depEnd = dep.getStartTime().plusHours(dep.getDuration());
                if (depEnd.isAfter(latestEnd)) {
                    latestEnd = depEnd;
                }
            }
        }

        return latestEnd;
    }

    // ✅ Removed: getScheduleForStudent(String indexNumber)
    // Use computeSchedule(tasks) after fetching by degreeId instead
}