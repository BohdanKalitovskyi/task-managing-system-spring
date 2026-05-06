package com.test.test;

import org.apache.coyote.BadRequestException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {



    private final Map<Long, Task> taskMap;
    private final AtomicLong idCounter;

    public TaskService(Map<Long, Task> taskHashMap) {
        taskMap = new HashMap<>();
        idCounter = new AtomicLong();
    }


    public Task getTaskById(
            Long id
    ) {
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        return taskMap.get(id);
    }

    public List<Task> findAllTasks() {
        return taskMap.values().stream().toList();
    }

    public Task createTask(
            Task taskToCreate
    ) {
        if(taskToCreate.id()!=null){
            throw new IllegalArgumentException("id should be empty");
        }

        var newTask = new Task(
                idCounter.incrementAndGet(),
                taskToCreate.creatorId(),
                taskToCreate.assignedUserId(),
                TaskStatus.CREATED,
                taskToCreate.createDateTime(),
                taskToCreate.deadlineDate(),
                taskToCreate.priority()
        );

        taskMap.put(newTask.id(),newTask);

        return newTask;
    }


    public Task updateTask(Long id, Task updatedTask) {
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        var task = taskMap.get(id);
        if(task.status() == TaskStatus.DONE)
        {
            throw new IllegalStateException("Cannot update task which is already done");
        }


        var newUpdatedTask = new Task(
                task.id(),
                updatedTask.creatorId(),
                updatedTask.assignedUserId(),
                updatedTask.status(),
                updatedTask.createDateTime(),
                updatedTask.deadlineDate(),
                updatedTask.priority()
        );

        taskMap.put(id, newUpdatedTask);

        return newUpdatedTask;
    }

    public void deleteTask(Long id) {
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        taskMap.remove(id);
    }

    public Task undoTask(Long id) {
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        var taskToUndo = taskMap.get(id);
        if(taskToUndo.status()!=TaskStatus.DONE)
        {
            throw new IllegalStateException("Cannot undo task that is not done yet");
        }

        var patchedTask =  new Task(
                taskToUndo.id(),
                taskToUndo.creatorId(),
                taskToUndo.assignedUserId(),
                TaskStatus.IN_PROGRESS,
                taskToUndo.createDateTime(),
                taskToUndo.deadlineDate(),
                taskToUndo.priority()
        );

        taskMap.put(id, patchedTask);

        return patchedTask;
    }
}
