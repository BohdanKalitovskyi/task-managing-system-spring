package com.test.test.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record Task(

        @Null
        Long id,

        @NotNull
        Long creatorId,

        @NotNull
        Long assignedUserId,

        TaskStatus status,

        @Null
        LocalDateTime createDateTime,

        @NotNull
        @Future
        LocalDate deadlineDate,

        @NotNull
        TaskPriority priority,

        LocalDateTime doneDateTime
) {
}
