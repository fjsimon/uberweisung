package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import com.fjsimon.uberweisung.repository.UserRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import com.fjsimon.uberweisung.validation.CheckOwnershipGet;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class CheckOwnershipGetValidator implements ConstraintValidator<CheckOwnershipGet, GetTransactionsRequest> {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private CheckOwnershipGet annotation;

    @Override
    public void initialize(CheckOwnershipGet constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(GetTransactionsRequest request, ConstraintValidatorContext context) {

        Optional<Wallet> wallet = walletRepository.findById(request.getWalletId());
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        return wallet.isPresent() && user.isPresent() ? wallet.get().getUserId().equals(user.get().getId()) : false;
    }
}