package com.test.test;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskService {



    private final Map<Long, Task> taskHashMap = Map.of(
            1L, new Task(
                    1L,
                    10L,
                    30L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDate.now().plusDays(2),
                    TaskPriority.LOW
            ),
            2L, new Task(
                    2L,
                    13L,
                    35L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDate.now().plusDays(3),
                    TaskPriority.LOW
            ),
            3L, new Task(
                    3L,
                    17L,
                    32L,
                    TaskStatus.CREATED,
                    LocalDateTime.now(),
                    LocalDate.now().plusDays(1),
                    TaskPriority.LOW
            )
    );


    public Task getTaskById(
            Long id
    ) {
        if(!taskHashMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        return taskHashMap.get(id);
    }

    public List<Task> findAllTasks() {
        return taskHashMap.values().stream().toList();
    }
}
