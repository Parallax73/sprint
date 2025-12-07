package com.sprint.core_api.dto.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
