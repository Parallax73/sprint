package com.sprint.core_api.dto.response;

import com.sprint.core_api.enums.Severity;

import java.util.UUID;

public record RuleResponse(

        UUID id,
        String name,
        Severity severity,
        boolean isActive




) {
}
