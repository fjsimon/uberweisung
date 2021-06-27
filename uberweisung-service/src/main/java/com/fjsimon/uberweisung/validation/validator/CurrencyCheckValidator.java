package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.validation.CurrencyCheck;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class CurrencyCheckValidator implements ConstraintValidator<CurrencyCheck, String> {

    @Autowired
    private Table<String, String, BigDecimal> referenceRates;

    @Override
    public void initialize(CurrencyCheck constraintAnnotation) {}

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {

        if(currency.isEmpty()) {
            return handleError(constraintValidatorContext, "currency is required");
        }

        if(!referenceRates.columnKeySet().contains(currency)) {
            return handleError(constraintValidatorContext, String.format("currency [%s] is invalid", currency));
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
