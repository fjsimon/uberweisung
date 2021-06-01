package com.fjsimon.uberweisung.validation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public final class ValidationDelegate {

    public static <T> void validate(Validator validator, T request, Class<T>... groups) {

        Set<ConstraintViolation<T>> violations = validator.validate(request, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
