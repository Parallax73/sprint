package com.sprint.core_api.dto.request;

public record CreateLogRequest(
        String username,
        String action,
        String ipAddress,
        String status,
        String details
) {
}
