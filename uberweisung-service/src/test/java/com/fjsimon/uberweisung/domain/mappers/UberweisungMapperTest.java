package com.fjsimon.uberweisung.domain.mappers;

import com.fjsimon.uberweisung.domain.repository.Role;
import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
class UberweisungMapperTest {

    @InjectMocks
    private UberweisungMapperImpl testee;

    @Test
    public void map_should_return_null() {

        assertThat(testee.map(null), is(nullValue()));
    }

    @Test
    public void map_should_return_user_response() {
        User user = new User("username", "password", new Role(), "firstname", "lastname");

        UserResponse userResponse = testee.map(user);

        assertThat(userResponse.getUsername(), is("username"));
        assertThat(userResponse.getFirstName(), is("firstname"));
        assertThat(userResponse.getLastName(), is("lastname"));
    }

    @Test
    public void mapTransaction_should_return_null() {

        assertThat(testee.mapTransaction(null), is(nullValue()));
    }

    @Test
    public void mapTransaction_should_return_get_transaction_response() {

        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction();
        transaction.setTransaction_type(TRANSACTION_TYPE.C);
        transaction.setGlobalId("globalId");
        transaction.setAmount(new BigDecimal(10));
        transaction.setDescription("description");
        transaction.setLastUpdated(now);

        GetTransactionResponse transactionResponse = testee.mapTransaction(transaction);

        assertThat(transactionResponse.getTransactionType(), is("C"));
        assertThat(transactionResponse.getGlobalId(), is("globalId"));
        assertThat(transactionResponse.getAmount(), is(new BigDecimal(10)));
        assertThat(transactionResponse.getDescription(), is("description"));
        assertThat(transactionResponse.getLastUpdated(), is(now));
    }

    @Test
    public void mapTransactions_should_return_null() {

        assertThat(testee.mapTransactions(null), is(nullValue()));
    }

    @Test
    public void mapTransactions() {

        Transaction transaction = new Transaction();
        transaction.setTransaction_type(TRANSACTION_TYPE.C);
        transaction.setDescription("description");

        List<GetTransactionResponse> transactionResponses = testee.mapTransactions(Arrays.asList(transaction));

        assertThat(transactionResponses.size(), is(1));
    }
}