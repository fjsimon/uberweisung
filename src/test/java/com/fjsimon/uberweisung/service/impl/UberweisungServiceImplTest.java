package com.fjsimon.uberweisung.service.impl;

import com.fjsimon.uberweisung.domain.mappers.UberweisungMapper;
import com.fjsimon.uberweisung.domain.repository.CURRENCY;
import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.repository.TransactionRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import com.fjsimon.uberweisung.service.UberweisungService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UberweisungServiceImplTest {

    private WalletRepository walletRepository = Mockito.mock(WalletRepository.class);

    private TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

    private UberweisungMapper uberweisungMapper = Mockito.mock(UberweisungMapper.class);

    private Validator validator = Mockito.mock(Validator.class);

    private UberweisungService testee;

    ArgumentCaptor<Wallet> walletArgumentCaptor = ArgumentCaptor.forClass(Wallet.class);

    @BeforeEach
    public void init() {

        testee = new UberweisungServiceImpl(walletRepository, transactionRepository, uberweisungMapper, validator);
    }

    @Test
    public void createTransaction_credit_amount() {

        Wallet wallet = getWallet();
        when(walletRepository.getOne(anyInt())).thenReturn(wallet);
        when(walletRepository.save(ArgumentMatchers.any())).thenReturn(wallet);
        when(transactionRepository.save(ArgumentMatchers.any())).thenReturn(getTransaction());

        CreateTransactionResponse response = testee.createTransaction(generateRequest(TRANSACTION_TYPE.C, new BigDecimal(100)));

        verify(walletRepository, times(1)).save(walletArgumentCaptor.capture());
        assertThat(walletArgumentCaptor.getValue().getBalance().intValue(), is(1100));
        assertThat(response.getGlobalId(), is("globalId"));
    }

    @Test
    public void createTransaction_debit_amount() {

        Wallet wallet = getWallet();
        when(walletRepository.getOne(anyInt())).thenReturn(wallet);
        when(walletRepository.save(ArgumentMatchers.any())).thenReturn(wallet);
        when(transactionRepository.save(ArgumentMatchers.any())).thenReturn(getTransaction());

        CreateTransactionResponse response = testee.createTransaction(generateRequest(TRANSACTION_TYPE.D, new BigDecimal(100)));

        verify(walletRepository, times(1)).save(walletArgumentCaptor.capture());
        assertThat(walletArgumentCaptor.getValue().getBalance().intValue(), is(900));
        assertThat(response.getGlobalId(), is("globalId"));
    }

    @Test
    public void generateGlobalIdTest() {

        String response = testee.generateGlobalId();
        assertThat(response, is(notNullValue()));
    }

    private CreateTransactionRequest generateRequest(TRANSACTION_TYPE transaction_type, BigDecimal amount) {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAmount(amount);
        request.setTransactionType(transaction_type.name());
        request.setWalletId(1);
        request.setGlobalId("123456789");
        request.setUsername("user");
        return request;
    }

    private Wallet getWallet() {

        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(new BigDecimal(1000));
        wallet.setUserId(1L);
        wallet.setCurrency(CURRENCY.EUR);
        return wallet;
    }

    private Transaction getTransaction() {

        Transaction transaction = new Transaction();
        transaction.setGlobalId("globalId");
        return transaction;
    }
}