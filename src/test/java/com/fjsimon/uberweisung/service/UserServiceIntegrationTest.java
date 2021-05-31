package com.fjsimon.uberweisung.service;

import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserServiceImpl service;

    @Test
    public void signin() {

        Optional<String> token = service.signin("user_test", "dummy");
        assertThat(token.isPresent(), is(false));
    }

    @Test
    public void signup() {

        Optional<User> user = service.signup("dummyUsername", "dummypassword", "joe", "doe");
        assertThat(user.get().getPassword(), not("dummypassword"));
        System.out.println("Encoded Password = " + user.get().getPassword());
    }

    @Test
    public void getAll() {

        assertThat(service.getAll().size(), is(2));
    }
}