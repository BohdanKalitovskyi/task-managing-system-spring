package com.test.test.task;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
           @PathVariable("id") Long id
    ){
        log.info("called getTaskById");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
    ){
        log.info("called getAllById");
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid Task taskToCreate
    ){
        log.info("called createTask");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("test-header","123")
                .body(taskService.createTask(taskToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long id,
            @RequestBody @Valid Task updatedTask
    ){
        log.info("called updateTask");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.updateTask(id,updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("called deleteTask");

            taskService.deleteTask(id);
            return ResponseEntity.ok()
                    .build();
    }

    @PatchMapping("/{id}/undo")
    public ResponseEntity<Task> undoTask(
            @PathVariable("id") Long id
    ){
        log.info("called undoTask");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.undoTask(id));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<Task> startTask(
            @PathVariable("id") Long id
    ){
        log.info("called startTask");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.startTask(id));
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Task> finishTask(
            @PathVariable("id") Long id
    ){
        log.info("called finishTask");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.finishTask(id));
    }
}
