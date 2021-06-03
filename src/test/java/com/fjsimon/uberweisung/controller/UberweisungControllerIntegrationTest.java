package com.fjsimon.uberweisung.controller;

import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.service.UberweisungService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UberweisungControllerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;

    @MockBean
    private UberweisungService service;

    @Test
    public void getTransactionsByWalletId() {

        Map<String, String> params = new HashMap<>();
        params.put("walletId", "1");

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        GetTransactionResponse response = GetTransactionResponse.builder()
                .globalId("globalId")
                .amount(new BigDecimal(12.0))
                .description("description")
                .lastUpdated(LocalDateTime.now())
                .transactionType("C")
                .build();

        when(service.getTransactions(any())).thenReturn(Arrays.asList(response));

        restTemplate.exchange("/uberweisung/account/{walletId}/transactions",
                HttpMethod.GET, entity, GetTransactionResponse[].class, params);

        verify(service, times(1)).getTransactions(any());
    }

    @Test
    public void getGlobalId(){

        Map<String, String> params = new HashMap<>();
        params.put("walletId", "1");

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        when(service.generateGlobalId()).thenReturn("globalId");

        restTemplate.exchange("/uberweisung/account/globalId",
                HttpMethod.GET, entity, String.class, params);

        verify(service, times(1)).generateGlobalId();
    }

    @Test
    public void createWalletTransaction(){

        Map<String, String> params = new HashMap<>();
        params.put("walletId", "1");

        CreateTransactionResponse response = new CreateTransactionResponse();
        response.setGlobalId("globalId");
        when(service.createTransaction(any(CreateTransactionRequest.class))).thenReturn(response);

        CreateTransactionRequest request = new CreateTransactionRequest();
        HttpEntity entity = new HttpEntity(request, jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<CreateTransactionResponse> restResponse = restTemplate
                .exchange("/uberweisung/account/{walletId}/transactions", POST,
                entity, CreateTransactionResponse.class, params);

        assertThat(restResponse.getStatusCode().value(), is(200));
        assertThat(restResponse.getBody().getGlobalId(), is("globalId"));
    }

}