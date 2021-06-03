package com.fjsimon.uberweisung.validation;

import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationDelegateTest {

    private ValidationDelegate testee;

    @BeforeEach
    public void validationDelegate_init() {
        testee = new ValidationDelegate();
    }

    @Test
    public void validate_test() {

        Validator validator = Mockito.mock(Validator.class);
        GetTransactionsRequest request = new GetTransactionsRequest();

        testee.validate(validator, request);

        verify(validator, times(1)).validate(request);
    }

    @Test
    public void validate_exception_test() {

        Validator validator = Mockito.mock(Validator.class);
        GetTransactionsRequest request = new GetTransactionsRequest();

        ConstraintViolation<Object> constraintViolation = Mockito.mock(ConstraintViolation.class);
        Path path = Mockito.mock(Path.class);
        when(path.toString()).thenReturn("test.property.path");
        when(constraintViolation.getPropertyPath()).thenReturn(path);
        when(constraintViolation.getMessage()).thenReturn("message");
        Set<ConstraintViolation<Object>> set = new HashSet<>();
        set.add(constraintViolation);
        when(validator.validate(any())).thenReturn(set);

        ConstraintViolationException thrown = assertThrows(
                ConstraintViolationException.class, () -> {
                    testee.validate(validator, request);
                });

        assertThat(thrown.getMessage(), is("test.property.path: message"));
        verify(validator, times(1)).validate(request);
    }
}