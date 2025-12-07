package com.sprint.core_api.dto.response;

import com.sprint.core_api.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String fullName,
        UserRole role,
        Instant createdAt,
        Instant updatedAt

) {
}
