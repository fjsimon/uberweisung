package com.fjsimon.uberweisung.controller;

import com.fjsimon.uberweisung.domain.mappers.UberweisungMapper;
import com.fjsimon.uberweisung.domain.repository.Role;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.service.request.LoginRequest;
import com.fjsimon.uberweisung.domain.service.response.UserResponse;
import com.fjsimon.uberweisung.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    private LoginRequest signupDto = new LoginRequest("larry", "1234", "larry", "miller");
    private UserResponse userResponse = UserResponse.builder().username("larry")
            .firstName("larry").lastName("miller").build();
    private User user = new User(signupDto.getUsername(), signupDto.getPassword(), new Role(),
            signupDto.getFirstName(), signupDto.getLastName());

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;

    @MockBean
    private UserServiceImpl service;

    @MockBean
    private UberweisungMapper mapper;

    @Test
    public void signin_success() {

        when(service.signin("admin", "myPass")).thenReturn(Optional.of("token"));

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/users/signin",
                new LoginRequest("admin", "myPass", "firstName", "secondName"),
                String.class);

        verify(this.service).signin("admin","myPass");
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is("token"));
    }

    @Test
    public void signin_failed() {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/users/signin",
                new LoginRequest("admin", "myPass", "firstName", "secondName"),
                String.class);

        verify(this.service).signin("admin","myPass");
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void signup_created() {

        when(service.signup(signupDto.getUsername(), signupDto.getPassword(), signupDto.getFirstName(),
                signupDto.getLastName())).thenReturn(Optional.of(user));
        when(mapper.map(eq(user))).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = restTemplate.exchange("/users/signup",
                POST,
                new HttpEntity(signupDto, jwtRequestHelper.withRole("ROLE_ADMIN")),
                UserResponse.class);

        verify(this.service, times(1))
                .signup(eq("larry"), eq("1234"), eq("larry"), eq("miller"));
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getUsername(), is(userResponse.getUsername()));
        assertThat(response.getBody().getFirstName(), is(userResponse.getFirstName()));
        assertThat(response.getBody().getLastName(), is(userResponse.getLastName()));
    }

    @Test
    public void signup_bad_request(){
        when(service.signup(signupDto.getUsername(), signupDto.getPassword(), signupDto.getFirstName(),
                signupDto.getLastName())).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = restTemplate.exchange("/users/signup",
                POST,
                new HttpEntity(signupDto, jwtRequestHelper.withRole("ROLE_ADMIN")),
                UserResponse.class);

        verify(this.service, times(1))
                .signup(eq("larry"), eq("1234"), eq("larry"), eq("miller"));
        verifyZeroInteractions(mapper);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void signup_unauthorized(){

        ResponseEntity<User> response = restTemplate.exchange("/users/signup",
                POST,
                new HttpEntity(signupDto,jwtRequestHelper.withRole("ROLE_CSR")),
                User.class);

        verifyZeroInteractions(service);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getAllUsers_success() {
        when(service.getAll()).thenReturn(Arrays.asList(user));

        ResponseEntity<List<User>> response = restTemplate.exchange("/users",
                GET,
                new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN")),
                new ParameterizedTypeReference<List<User>>() {});

        verify(this.service, times(1)).getAll();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().get(0).getUsername(), is(user.getUsername()));
    }
}