package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.validation.BigDecimalFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class BigDecimalFormatValidator implements ConstraintValidator<BigDecimalFormat, String> {

    @Override
    public void initialize(BigDecimalFormat constraintAnnotation) {

    }

    @Override
    public boolean isValid(String amount, ConstraintValidatorContext constraintValidatorContext) {

        if(amount.isEmpty()) {
            return handleError(constraintValidatorContext, "amount is required");
        }

        try {
            new BigDecimal(amount);
        } catch (NumberFormatException ex) {
            return handleError(constraintValidatorContext, "amount format invalid");
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
