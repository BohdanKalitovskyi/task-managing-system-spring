package com.test.test.task;

import org.springframework.web.bind.annotation.RequestParam;

public record TaskSearchFiler(
         Long creatorId,
         Long assignedUserId,
         TaskStatus status,
         TaskPriority priority,
         Integer pageSize,
         Integer pageNumber
) {
}
