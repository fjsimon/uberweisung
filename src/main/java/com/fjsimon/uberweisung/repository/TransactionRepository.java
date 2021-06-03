package com.fjsimon.uberweisung.repository;

import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.Wallet;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByWallet(Wallet wallet);

    List<Transaction> findByWalletId(Integer walletId);
}