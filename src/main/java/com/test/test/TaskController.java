package com.test.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public Task getTaskById(
           @PathVariable("id") Long id
    ){
        System.out.println("by id");
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<Task> getAllTasks(
    ){
        System.out.println("all");
        return taskService.findAllTasks();
    }
}
