package com.sprint.core_api.utils.spel;

import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpelExpressionValidator implements ConstraintValidator<ValidSpel, String> {

    // The parser is thread-safe and can be reused
    private static final SpelExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotNull handle empty values
        }
        try {
            // This attempts to parse the string.
            // If the syntax is wrong (e.g. mismatched parenthesis), it throws an exception.
            parser.parseExpression(value);
            return true;
        } catch (ParseException e) {
            // Optional: Add the specific error message to the context
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid SpEL: " + e.getSimpleMessage())
                    .addConstraintViolation();
            return false;
        }
    }
}