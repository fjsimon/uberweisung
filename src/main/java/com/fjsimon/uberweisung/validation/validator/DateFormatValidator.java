package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.validation.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

    @Override
    public void initialize(DateFormat constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s.isEmpty()) {
            return handleError(constraintValidatorContext, "date is required");
        }

        try {
            LocalDate.parse(s);
        } catch (DateTimeParseException ex) {
            return handleError(constraintValidatorContext, "date format invalid [yyyy-MM-dd]");
        }

        return true;
    }

    private boolean handleError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
