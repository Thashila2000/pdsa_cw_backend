package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.entity.TaskEntity;
import com.hdse242052.lms_final.dto.TaskDto;
import com.hdse242052.lms_final.repository.TaskRepository;
import com.hdse242052.lms_final.service.TaskService;
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

    // ✅ Create a task with optional dependency-based start time
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

    // ✅ Get tasks, optionally filtered by degreeId
    @GetMapping
    public List<TaskEntity> getTasks(@RequestParam(required = false) Long degreeId) {
        return (degreeId != null)
                ? taskRepo.findByDegreeId(degreeId)
                : taskRepo.findAll();
    }


    // ✅ Get scheduled task DTOs — filtered by degreeId only
    @GetMapping("/schedule")
    public List<TaskDto> getScheduledTasks(@RequestParam(required = false) Long degreeId) {
        List<TaskEntity> tasks = (degreeId != null)
                ? taskRepo.findByDegreeId(degreeId)
                : taskRepo.findAll();

        return scheduler.computeSchedule(tasks);
    }

    // ✅ Delete task by ID
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        if (!taskRepo.existsById(id)) {
            return "Task not found";
        }

        taskRepo.deleteById(id);
        return "Task deleted";
    }

    // ✅ Update task by ID
    @PutMapping("/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody TaskEntity updatedTask) {
        return taskRepo.findById(id).map(existingTask -> {
            List<TaskEntity> allTasks = taskRepo.findAll();

            existingTask.setName(updatedTask.getName());
            existingTask.setDuration(updatedTask.getDuration());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDependencies(updatedTask.getDependencies());
            existingTask.setDegree(updatedTask.getDegree());

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