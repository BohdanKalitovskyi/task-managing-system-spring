package com.test.test.task;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TaskEntity() {
    }

    public TaskEntity(Long id, Long creatorId, Long assignedUserId, TaskStatus status, LocalDateTime createDateTime, LocalDate deadlineDate, TaskPriority priority, LocalDateTime doneDateTime) {
        this.id = id;
        this.creatorId = creatorId;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.createDateTime = createDateTime;
        this.deadlineDate = deadlineDate;
        this.priority = priority;
        this.doneDateTime = doneDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "assigned_user_id", nullable = false)
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDateTime;

    @Column(name = "deadline_date", nullable = false)
    private LocalDate deadlineDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Column(name = "done_time", nullable = true)
    private LocalDateTime doneDateTime;

    public LocalDateTime getDoneDateTime() {
        return doneDateTime;
    }

    public void setDoneDateTime(LocalDateTime doneDateTime) {
        this.doneDateTime = doneDateTime;
    }
}
