package com.sprint.core_api.dto.request;

import com.sprint.core_api.enums.Severity;
import com.sprint.core_api.utils.spel.ValidSpel;

public record CreateRuleRequest(

        String name,
        String description,
        @ValidSpel
        String conditionLogic,
        Severity severity,
        boolean isActive

) {
}
