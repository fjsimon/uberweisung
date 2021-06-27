package com.fjsimon.uberweisung.validation;

import com.fjsimon.uberweisung.validation.validator.CheckOwnershipGetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {CheckOwnershipGetValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface CheckOwnershipGet {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}