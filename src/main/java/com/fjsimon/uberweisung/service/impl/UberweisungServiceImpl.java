package com.fjsimon.uberweisung.service.impl;

import com.fjsimon.uberweisung.domain.mappers.UberweisungMapper;
import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.repository.TransactionRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import com.fjsimon.uberweisung.service.UberweisungService;

import com.fjsimon.uberweisung.validation.ValidationDelegate;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UberweisungServiceImpl implements UberweisungService {

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final UberweisungMapper uberweisungMapper;

    private final Validator validator;

    @Override
    @Transactional
    public List<GetTransactionResponse> getTransactions(@Valid GetTransactionsRequest request) {

        ValidationDelegate.validate(validator, request);

        List<Transaction> transactions = transactionRepository.findByWalletId(request.getWalletId());

        return uberweisungMapper.mapTransactions(transactions);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public CreateTransactionResponse createTransaction(@Valid CreateTransactionRequest request) {

        ValidationDelegate.validate(validator, request);

        Wallet wallet = saveWallet(request);

        Transaction transaction = saveTransaction(request, wallet);

        return CreateTransactionResponse.builder()
                .globalId(transaction.getGlobalId()).build();
    }

    @Override
    @Transactional
    public String generateGlobalId() {

        byte[] buffer = new byte[20];

        random.nextBytes(buffer);

        return encoder.encodeToString(buffer);
    }

    private Wallet saveWallet(CreateTransactionRequest request) {

        Wallet wallet = walletRepository.getOne(request.getWalletId());
        wallet.setBalance(calculateBalance(request,wallet));
        wallet.setLastUpdated(LocalDateTime.now());
        return walletRepository.save(wallet);
    }

    private BigDecimal calculateBalance(CreateTransactionRequest request, Wallet wallet) {

        MonetaryAmount amount = Money.of(request.getAmount(), wallet.getCurrency().name());
        MonetaryAmount currentBalance = Money.of(wallet.getBalance(), wallet.getCurrency().name());
        MonetaryAmount finalBalance = isCredit(request) ? currentBalance.add(amount) : currentBalance.subtract(amount);
        return new BigDecimal(finalBalance.getNumber().doubleValue());
    }

    private Transaction saveTransaction(CreateTransactionRequest request, Wallet wallet) {

        Transaction transaction = Transaction.builder()
                .transaction_type(TRANSACTION_TYPE.valueOf(request.getTransactionType()))
                .amount(request.getAmount())
                .globalId(request.getGlobalId())
                .description(request.getDescription())
                .lastUpdated(LocalDateTime.now())
                .wallet(wallet).build();

        return transactionRepository.save(transaction);
    }

    private boolean isCredit(CreateTransactionRequest request) {

        return TRANSACTION_TYPE.C.name().equalsIgnoreCase(request.getTransactionType());
    }
}
