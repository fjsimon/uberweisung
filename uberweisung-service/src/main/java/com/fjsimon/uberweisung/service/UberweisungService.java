package com.fjsimon.uberweisung.service;

import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;

import javax.validation.Valid;
import java.util.List;

public interface UberweisungService {

    List<GetTransactionResponse> getTransactions(@Valid GetTransactionsRequest request);

    CreateTransactionResponse createTransaction(@Valid CreateTransactionRequest request);

    String generateGlobalId();
}
