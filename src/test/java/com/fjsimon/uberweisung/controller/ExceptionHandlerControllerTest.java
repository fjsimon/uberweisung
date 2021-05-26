package com.fjsimon.uberweisung.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTest {

    @InjectMocks
    private ExceptionHandlerController testee;

    @Test
    public void handleAccessDeniedExceptionTest() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        testee.handleAccessDeniedException(new AccessDeniedException("access denied"), response);
        verify(response, times(1)).sendError(eq(403), eq("Access denied"));
    }

    @Test
    public void handleHttpServerErrorExceptionTest() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        testee.handleHttpServerErrorException(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), response);
        verify(response, times(1)).sendError(eq(503), eq("503 SERVICE_UNAVAILABLE"));
    }

    @Test
    public void handleInsufficientAuthenticationExceptionTest() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        testee.handleInsufficientAuthenticationException(new InsufficientAuthenticationException("auth exception"), response);
        verify(response, times(1)).sendError(eq(403), eq("Insufficient Authentication"));
    }

    @Test
    public void handleExceptionTest() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        testee.handleException(new InsufficientAuthenticationException("auth exception"), response);
        verify(response, times(1)).sendError(eq(500), eq("Something went wrong"));
    }

    @Test
    public void constraintViolationExceptionTest() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        testee.constraintViolationException(new ConstraintViolationException(Collections.emptySet()), response);
        verify(response, times(1)).sendError(eq(400), eq(""));
    }
}