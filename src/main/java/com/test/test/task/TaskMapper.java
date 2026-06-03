package com.test.test.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomain(
            TaskEntity taskEntity
    ){
        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getStatus(),
                taskEntity.getCreateDateTime(),
                taskEntity.getDeadlineDate(),
                taskEntity.getPriority(),
                taskEntity.getDoneDateTime()
        );
    }

    public TaskEntity toEntity(
            Task taskEntity
    ){
        return new TaskEntity(
                taskEntity.id(),
                taskEntity.creatorId(),
                taskEntity.assignedUserId(),
                taskEntity.status(),
                taskEntity.createDateTime(),
                taskEntity.deadlineDate(),
                taskEntity.priority(),
                taskEntity.doneDateTime()
        );
    }
}
