package com.sprint.core_api.dto.request;

import com.sprint.core_api.enums.UserRole;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        UserRole role,
        String fullName

) {
}
