package com.test.test.task;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

    long countByAssignedUserIdAndStatus(Long assignedUserId, TaskStatus status);

    @Query("""
            SELECT t from TaskEntity t
                WHERE t.creatorId = :creatorId
                AND t.assignedUserId = :assignedUserId
                AND t.status = :status
                AND t.priority = :priority
           """)

    List<TaskEntity> searchAllByFilter(
          @Param("creatorId") Long creatorId,
          @Param("assignedUserId") Long assignedUserId,
          @Param("status") TaskStatus status,
          @Param("priority") TaskPriority priority,
          Pageable pageable
    );
}
