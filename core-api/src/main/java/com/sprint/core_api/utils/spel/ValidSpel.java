package com.sprint.core_api.utils.spel;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpelExpressionValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSpel {
    String message() default "Invalid SpEL expression syntax";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}