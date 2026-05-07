package com.test.test.task;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private static final int MAX_TASKS_IN_PROGRESS = 5;

    private final TaskRepository repository;

    private final Map<Long, Task> taskMap;
    private final AtomicLong idCounter;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
        taskMap = new HashMap<>();
        idCounter = new AtomicLong();
    }


    public Task getTaskById(
            Long id
    ) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Entity not found "+id
                ));

        return toDomainTask(taskEntity);
    }

    public List<Task> findAllTasks() {
        List<TaskEntity> allEntities = repository.findAll();

        List<Task> taskList = allEntities.stream().map(
                this::toDomainTask)
                .toList();

        return taskList;
    }

    public Task createTask(
            Task taskToCreate
    ) {
        if(taskToCreate.id()!=null){
            throw new IllegalArgumentException("id should be empty");
        }
        if(taskToCreate.status()!=null){
            throw new IllegalArgumentException("status should be empty");
        }


        var entityToSave = new TaskEntity(
                null,
                taskToCreate.creatorId(),
                taskToCreate.assignedUserId(),
                TaskStatus.CREATED,
                taskToCreate.createDateTime(),
                taskToCreate.deadlineDate(),
                taskToCreate.priority()
        );

        var savedEntity = repository.save(entityToSave);
        return toDomainTask(savedEntity);
    }


    public Task updateTask(Long id, Task updatedTask) {
        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        if(taskEntity.getStatus() == TaskStatus.DONE)
        {
            throw new IllegalStateException("Cannot update task which is already done");
        }


        var newUpdatedTask = new TaskEntity(
                taskEntity.getId(),
                updatedTask.creatorId(),
                updatedTask.assignedUserId(),
                taskEntity.getStatus(),
                updatedTask.createDateTime(),
                updatedTask.deadlineDate(),
                updatedTask.priority()
        );
        var newTaskToUpdate = repository.save(newUpdatedTask);

        return toDomainTask(newTaskToUpdate);
    }

    public void deleteTask(Long id) {
        if(!repository.existsById(id)){
            throw new NoSuchElementException("Not found task by id = "+id);
        }
        repository.deleteById(id);
    }

    public Task undoTask(Long id) {

        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        if(taskEntity.getStatus()!=TaskStatus.DONE)
        {
            throw new IllegalStateException("Cannot undo task that is not done yet");
        }

        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        repository.save(taskEntity);

        return toDomainTask(taskEntity);
    }

    public Task startTask(Long id) {
        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        long currentCount = repository.countByAssignedUserIdAndStatus(
                taskEntity.getAssignedUserId(), TaskStatus.IN_PROGRESS);

        if(currentCount>=MAX_TASKS_IN_PROGRESS){
            throw new IllegalStateException("User cannot have more than 5 started tasks");
        }
        if(taskEntity.getStatus()!=TaskStatus.CREATED)
        {
            throw new IllegalStateException("Cannot start already started task");
        }

        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        repository.save(taskEntity);

        return toDomainTask(taskEntity);
    }

    public  Task finishTask(Long id) {
        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        if(taskEntity.getStatus()!=TaskStatus.IN_PROGRESS)
        {
            throw new IllegalStateException("Cannot finish not started or finished task");
        }

        taskEntity.setStatus(TaskStatus.DONE);
        repository.save(taskEntity);

        return toDomainTask(taskEntity);
    }

    private Task toDomainTask(
            TaskEntity taskEntity
    ){
        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getStatus(),
                taskEntity.getCreateDateTime(),
                taskEntity.getDeadlineDate(),
                taskEntity.getPriority()
        );
    }


}
