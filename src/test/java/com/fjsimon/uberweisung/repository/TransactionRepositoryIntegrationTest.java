package com.fjsimon.uberweisung.repository;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransactionRepositoryIntegrationTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    public void findByWallet_test() {
        Wallet wallet = new Wallet();
        wallet.setId(1);
        List<Transaction> transactions = repository.findByWallet(wallet);
        assertTrue(transactions.size() > 0);
    }

    @Test
    public void findByWalletId_test() {
        List<Transaction> transactions = repository.findByWalletId(1);
        assertTrue(transactions.size() > 0);
    }

    @Test
    public void credit_test() {

        Wallet wallet = new Wallet();
        wallet.setId(1);

        Transaction transaction = Transaction.builder()
            .globalId(UUID.randomUUID().toString())
            .transaction_type(TRANSACTION_TYPE.C)
            .amount(new BigDecimal(20)).wallet(wallet)
            .description("Credit transaction").build();

        Transaction created = repository.save(transaction);
        assertNotNull(created);

        Transaction retrievedTransaction = repository.getOne(created.getId());

        assertTrue(retrievedTransaction.getAmount().equals(new BigDecimal(20)));
        assertTrue(retrievedTransaction.getTransaction_type().equals(TRANSACTION_TYPE.C));
        assertTrue(retrievedTransaction.getWallet().getId().equals(wallet.getId()));
    }

    @Test
    public void debit_test() {

        Wallet wallet = new Wallet();
        wallet.setId(1);

        Transaction transaction = Transaction.builder()
                .globalId(UUID.randomUUID().toString())
                .transaction_type(TRANSACTION_TYPE.D)
                .amount(new BigDecimal(20)).wallet(wallet)
                .description("Debit transaction").build();

        Transaction created = repository.save(transaction);
        assertNotNull(created);

        Transaction retrievedTransaction = repository.getOne(created.getId());

        assertTrue(retrievedTransaction.getAmount().equals(new BigDecimal(20)));
        assertTrue(retrievedTransaction.getTransaction_type().equals(TRANSACTION_TYPE.D));
        assertTrue(retrievedTransaction.getWallet().getId().equals(wallet.getId()));
    }


}