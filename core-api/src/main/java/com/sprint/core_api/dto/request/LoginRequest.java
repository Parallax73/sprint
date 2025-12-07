package com.sprint.core_api.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
