package com.hdse242052.task_scheduler.controller;

import com.hdse242052.task_scheduler.entity.TaskEntity;
import com.hdse242052.task_scheduler.dto.TaskDto;
import com.hdse242052.task_scheduler.repository.TaskRepository;
import com.hdse242052.task_scheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private TaskService scheduler;

    @PostMapping
    public String addTask(@RequestBody TaskEntity task) {
        List<TaskEntity> allTasks = taskRepo.findAll();

        if (task.getDependencies() != null && !task.getDependencies().isEmpty()) {
            LocalDateTime computedStart = scheduler.computeStartTimeFromDependencies(task, allTasks);
            task.setStartTime(computedStart);
        }

        taskRepo.save(task);
        return "Task added";
    }

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        return taskRepo.findAll();
    }

    @GetMapping("/schedule")
    public List<TaskDto> getScheduledTasks() {
        List<TaskEntity> tasks = taskRepo.findAll();
        return scheduler.computeSchedule(tasks);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        if (taskRepo.existsById(id)) {
            taskRepo.deleteById(id);
            return "Task deleted";
        } else {
            return "Task not found";
        }
    }

    @PutMapping("/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody TaskEntity updatedTask) {
        return taskRepo.findById(id).map(existingTask -> {
            List<TaskEntity> allTasks = taskRepo.findAll();

            existingTask.setName(updatedTask.getName());
            existingTask.setDuration(updatedTask.getDuration());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDependencies(updatedTask.getDependencies());

            if (updatedTask.getDependencies() != null && !updatedTask.getDependencies().isEmpty()) {
                LocalDateTime computedStart = scheduler.computeStartTimeFromDependencies(updatedTask, allTasks);
                existingTask.setStartTime(computedStart);
            } else {
                existingTask.setStartTime(updatedTask.getStartTime());
            }

            taskRepo.save(existingTask);
            return "Task updated";
        }).orElse("Task not found");
    }
}