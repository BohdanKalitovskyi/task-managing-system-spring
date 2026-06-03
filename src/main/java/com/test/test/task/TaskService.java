package com.test.test.task;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private static final int MAX_TASKS_IN_PROGRESS = 5;

    private final TaskRepository repository;

    private final TaskMapper mapper;

    private final Map<Long, Task> taskMap;
    private final AtomicLong idCounter;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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

        return mapper.toDomain(taskEntity);
    }

    public List<Task> searchAllByFilter(
            TaskSearchFiler filer
    ) {
        int pageSize = filer.pageSize() != null
                ? filer.pageSize() : 10;
        int pageNumber = filer.pageNumber() != null
                ? filer.pageNumber() : 0;

        var pageable = Pageable
                .ofSize(pageSize)
                .withPage(pageNumber);

        List<TaskEntity> allEntities = repository.searchAllByFilter(
                filer.creatorId(),
                filer.assignedUserId(),
                filer.status(),
                filer.priority(),
                pageable
        );

        List<Task> taskList = allEntities.stream().map(
                mapper::toDomain)
                .toList();

        return taskList;
    }

    public Task createTask(
            Task taskToCreate
    ) {
        if(taskToCreate.status()!=null){
            throw new IllegalArgumentException("status should be empty");
        }

        var entityToSave = mapper.toEntity(taskToCreate);
        entityToSave.setStatus(TaskStatus.CREATED);
        entityToSave.setCreateDateTime(LocalDateTime.now());
        entityToSave.setDoneDateTime(null);

        var savedEntity = repository.save(entityToSave);
        return mapper.toDomain(savedEntity);
    }


    public Task updateTask(Long id, Task updatedTask) {
        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        if(taskEntity.getStatus() == TaskStatus.DONE)
        {
            throw new IllegalStateException("Cannot update task which is already done");
        }


        var newUpdatedTask = mapper.toEntity(updatedTask);
        newUpdatedTask.setId(taskEntity.getId());
        newUpdatedTask.setStatus(taskEntity.getStatus());
        newUpdatedTask.setCreateDateTime(taskEntity.getCreateDateTime());
        newUpdatedTask.setDoneDateTime(null);

        var newTaskToUpdate = repository.save(newUpdatedTask);

        return mapper.toDomain(newTaskToUpdate);
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

        return mapper.toDomain(taskEntity);
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

        return mapper.toDomain(taskEntity);
    }

    public  Task finishTask(Long id) {
        var taskEntity = repository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Not found task by id = "+id));

        if(taskEntity.getStatus()!=TaskStatus.IN_PROGRESS)
        {
            throw new IllegalStateException("Cannot finish not started or finished task");
        }

        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setDoneDateTime(LocalDateTime.now());
        repository.save(taskEntity);

        return mapper.toDomain(taskEntity);
    }




}
