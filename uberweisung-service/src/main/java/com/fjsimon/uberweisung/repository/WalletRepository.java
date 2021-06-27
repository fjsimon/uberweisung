package com.fjsimon.uberweisung.repository;

import com.fjsimon.uberweisung.domain.repository.Wallet;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    List<Wallet> findByUserId(Long userId);
}