package com.hdse242052.task_scheduler.service;

import com.hdse242052.task_scheduler.entity.TaskEntity;
import com.hdse242052.task_scheduler.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    // Computes the full schedule with dependency-aware start and end times
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
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setDuration(task.getDuration());
            dto.setComputedStart(start);
            dto.setComputedEnd(start.plusHours(task.getDuration()));
            result.add(dto);
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
            LocalDateTime depEnd = visit(depId, taskMap, startTimes, visited)
                    .plusHours(taskMap.get(depId).getDuration());
            if (depEnd.isAfter(maxEnd)) {
                maxEnd = depEnd;
            }
        }

        startTimes.put(id, maxEnd);
        return maxEnd;
    }

    // Computes the correct start time for a task based on its dependencies
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
                LocalDateTime depEnd = dep.getStartTime().plusHours(dep.getDuration());
                if (depEnd.isAfter(latestEnd)) {
                    latestEnd = depEnd;
                }
            }
        }

        return latestEnd;
    }
}