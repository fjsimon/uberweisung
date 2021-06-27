package com.fjsimon.uberweisung.repository;

import com.fjsimon.uberweisung.domain.repository.CURRENCY;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WalletRepositoryIntegrationTest {

    @Autowired
    private WalletRepository repository;

    @Test
    public void findByWallet_test() {

        List<Wallet> wallets = repository.findByUserId(1L);
        assertTrue(wallets.size() > 0);
    }

    @Test
    public void createWallet_test() {

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal(100.0));
        wallet.setCurrency(CURRENCY.EUR);
        wallet.setLastUpdated(LocalDateTime.now());
        wallet.setUserId(2L);

        List<Wallet> initialWallets = repository.findByUserId(2L);
        assertTrue(initialWallets.size() == 1);

        Wallet savedWallet = repository.save(wallet);

        List<Wallet> finalWallets = repository.findByUserId(2L);
        assertTrue(finalWallets.size() == 2);
    }


}