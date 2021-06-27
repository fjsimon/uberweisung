package com.fjsimon.uberweisung.service;

import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.service.impl.UberweisungServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UberweisungServiceIntegrationTest {

    @Autowired
    private UberweisungServiceImpl service;

    @Test
    public void getTransactions() {

        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setUsername("admin");
        request.setWalletId(1);

        List<GetTransactionResponse> transactions = service.getTransactions(request);
        assertThat(transactions.size(), is(3));
    }

    @Test(expected = ValidationException.class)
    public void createTransaction_exception() {

        service.createTransaction(new CreateTransactionRequest());
    }

    @Test
    public void createTransaction() {

        CreateTransactionResponse response = service.createTransaction(generateCreateTransactionRequest());
        List<GetTransactionResponse> transactions = service.getTransactions(generateGetTransactionsRequest());
        assertThat(transactions.size(), is(4));
        assertThat(response.getGlobalId(), is("5ec332c7-9d8c-3a95-8478-a8d16c6a7cd3"));
    }

    @Test
    public void generateGlobalId() {

        String generateGlobalId = service.generateGlobalId();
        assertThat(generateGlobalId, is(notNullValue()));
    }

    @Test
    public void generateGlobalId_returns_different_values() {

        String generateGlobalId10 = service.generateGlobalId();
        String generateGlobalId11 = service.generateGlobalId();
        System.out.println(generateGlobalId10);
        assertThat(generateGlobalId10, is(not(equalTo(generateGlobalId11))));
    }

    private GetTransactionsRequest generateGetTransactionsRequest() {

        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setUsername("admin");
        request.setWalletId(1);
        return request;
    }

    private CreateTransactionRequest generateCreateTransactionRequest() {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(1);
        request.setUsername("admin");
        request.setGlobalId("5ec332c7-9d8c-3a95-8478-a8d16c6a7cd3");
        request.setTransactionType("C");
        request.setAmount(new BigDecimal(10));
        request.setDescription("adding money");
        return request;
    }
}
