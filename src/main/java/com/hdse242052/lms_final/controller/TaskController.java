package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.TaskDto;
import com.hdse242052.lms_final.entity.TaskEntity;
import com.hdse242052.lms_final.repository.TaskRepository;
import com.hdse242052.lms_final.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private TaskService scheduler;

    // ✅ Create a task with optional dependency-based start time
    @PostMapping
    public ResponseEntity<TaskEntity> addTask(@RequestBody TaskEntity task) {
        List<TaskEntity> allTasks = taskRepo.findAll();

        LocalDateTime computedStart = (task.getDependencies() != null && !task.getDependencies().isEmpty())
                ? scheduler.computeStartTimeFromDependencies(task, allTasks)
                : (task.getStartTime() != null ? task.getStartTime() : LocalDateTime.now());

        task.setStartTime(computedStart);
        TaskEntity saved = taskRepo.save(task);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all tasks or filter by degreeId
    @GetMapping
    public ResponseEntity<List<TaskEntity>> getTasks(@RequestParam(required = false) Long degreeId) {
        List<TaskEntity> tasks = (degreeId != null)
                ? taskRepo.findByDegreeId(degreeId)
                : taskRepo.findAll();
        return ResponseEntity.ok(tasks);
    }

    // ✅ Get scheduled task DTOs (computed timeline)
    @GetMapping("/schedule")
    public ResponseEntity<List<TaskDto>> getScheduledTasks(@RequestParam(required = false) Long degreeId) {
        List<TaskEntity> tasks = (degreeId != null)
                ? taskRepo.findByDegreeId(degreeId)
                : taskRepo.findAll();

        List<TaskDto> scheduled = scheduler.computeSchedule(tasks);
        return ResponseEntity.ok(scheduled);
    }

    // ✅ Delete task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (!taskRepo.existsById(id)) {
            return ResponseEntity.status(404).body("Task not found");
        }

        taskRepo.deleteById(id);
        return ResponseEntity.ok("Task deleted");
    }

    // ✅ Update task by ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody TaskEntity updatedTask) {
        return taskRepo.findById(id).map(existingTask -> {
            List<TaskEntity> allTasks = taskRepo.findAll();

            existingTask.setName(updatedTask.getName());
            existingTask.setDuration(updatedTask.getDuration());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDependencies(updatedTask.getDependencies());
            existingTask.setDegree(updatedTask.getDegree());

            LocalDateTime computedStart = (updatedTask.getDependencies() != null && !updatedTask.getDependencies().isEmpty())
                    ? scheduler.computeStartTimeFromDependencies(updatedTask, allTasks)
                    : (updatedTask.getStartTime() != null ? updatedTask.getStartTime() : LocalDateTime.now());

            existingTask.setStartTime(computedStart);
            taskRepo.save(existingTask);
            return ResponseEntity.ok("Task updated");
        }).orElse(ResponseEntity.status(404).body("Task not found"));
    }
}