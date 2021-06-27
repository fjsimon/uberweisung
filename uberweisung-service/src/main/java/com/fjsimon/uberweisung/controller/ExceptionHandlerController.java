package com.fjsimon.uberweisung.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @Bean
    public ErrorAttributes errorAttributes() {

        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse res) throws IOException {

        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public void handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, HttpServletResponse res) throws IOException {

        LOGGER.error("Handled Insufficient Authentication Exception", ex);
        res.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient Authentication");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletResponse res) throws IOException {

        LOGGER.error("Handled Internal Error Exception", ex);
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(ConstraintViolationException ex, HttpServletResponse res) throws IOException {

        LOGGER.error("Handled Constraint Violation Exception", ex);
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public void handleArithmeticException(ArithmeticException ex,  HttpServletResponse res) throws IOException {

        LOGGER.error("ArithmeticException");
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationErrors(MethodArgumentNotValidException ex, HttpServletResponse res) throws IOException {

        LOGGER.error("MethodArgumentNotValidException", ex);
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Argument Not Valid");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public void handleHttpServerErrorException(HttpServerErrorException ex, HttpServletResponse res) throws IOException {

        LOGGER.error("HttpServerErrorException", ex);
        res.sendError(ex.getStatusCode().value(),ex.getMessage());
    }
}

