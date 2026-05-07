package com.test.test.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
    long countByAssignedUserId(Long assignedUserId);
    long countByAssignedUserIdAndStatus(Long assignedUserId, TaskStatus status);
}
