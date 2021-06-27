package com.fjsimon.uberweisung.controller;

import com.fjsimon.uberweisung.domain.service.request.LoginRequest;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class LoginRequestTest {

    @Test
    public void testAll() {

        LoginRequest dto = new LoginRequest("user","pwd", null, null);
        assertThat(dto.getUsername(), is("user"));
        assertThat(dto.getPassword(), is("pwd"));
    }
}