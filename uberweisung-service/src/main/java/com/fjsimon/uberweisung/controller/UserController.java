package com.fjsimon.uberweisung.controller;

import com.fjsimon.uberweisung.domain.mappers.UberweisungMapper;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.service.request.LoginRequest;
import com.fjsimon.uberweisung.domain.service.response.UserResponse;
import com.fjsimon.uberweisung.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    private final UberweisungMapper mapper;

    @PostMapping("/signin")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {

        LOGGER.info("POST login");

        return userService.signin(loginRequest.getUsername(), loginRequest.getPassword())
                .orElseThrow(()-> new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@RequestBody @Valid LoginRequest loginRequest) {

        LOGGER.info("POST signup");

        return mapper.map(userService.signup(loginRequest.getUsername(), loginRequest.getPassword(),
                loginRequest.getFirstName(), loginRequest.getLastName())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User already exists")));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {

        LOGGER.info("GET getAllUsers");

        return userService.getAll();
    }

}