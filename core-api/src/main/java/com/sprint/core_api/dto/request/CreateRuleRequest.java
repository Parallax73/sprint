package com.sprint.core_api.dto.request;

import com.sprint.core_api.enums.Severity;

public record CreateRuleRequest(

        String name,
        String description,
        String conditionLogic,
        Severity severity,
        boolean isActive

) {
}
