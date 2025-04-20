package com.sporty.iam.service.validator;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * Created by Tigran Melkonyan
 * Date: 2/18/25
 * Time: 11:36 PM
 */
@Component
@RequiredArgsConstructor
public class ModelValidator {

    private final Validator validator;

    public void validate(final Object request) {
        Assert.notNull(request, "request.object.is.required");
        final Set<ConstraintViolation<Object>> violations = this.validator.validate(request);
        if (!violations.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final ConstraintViolation<Object> violation : violations) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(", ");
                }
                stringBuilder
                        .append(violation.getPropertyPath())
                        .append(' ')
                        .append(violation.getMessage());
            }
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
