package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.repository.TransactionRepository;
import com.fjsimon.uberweisung.repository.UserRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import com.fjsimon.uberweisung.validation.CheckCreateTransaction;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class CheckCreateTransactionValidator implements ConstraintValidator<CheckCreateTransaction, CreateTransactionRequest> {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private CheckCreateTransaction annotation;

    @Override
    public void initialize(CheckCreateTransaction constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(CreateTransactionRequest request, ConstraintValidatorContext context) {

        Optional<Wallet> wallet = walletRepository.findById(request.getWalletId());
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(!checkOwnership(wallet, user)) {

            return error(context, "{wallet.owner.invalid}");

        } else if(!checkBalance(request, wallet)) {

            return error(context, "{operation.not.permitted}");

        } else if(!checkGlobalId(request, wallet)) {

            return error(context, "{global.id.mismatch}");

        }

        return true;
    }

    private boolean checkBalance(CreateTransactionRequest request, Optional<Wallet> wallet) {

        BigDecimal transactionAmount = isCredit(request) ?
                request.getAmount().abs() :
                request.getAmount().abs().negate();

        return (isCredit(request) || (wallet.isPresent() && wallet.get().getBalance().compareTo(transactionAmount.abs()) >= 0));
    }

    private boolean isCredit(CreateTransactionRequest request) {

        return TRANSACTION_TYPE.C.name().equalsIgnoreCase(request.getTransactionType());
    }

    private boolean checkOwnership(Optional<Wallet> wallet, Optional<User> user) {

        return (wallet.isPresent() && user.isPresent()) ? wallet.get().getUserId().equals(user.get().getId()) : false;
    }

    private boolean checkGlobalId(CreateTransactionRequest request, Optional<Wallet> wallet) {

        StringBuilder builder = new StringBuilder();
        if(wallet.isPresent()) {
            builder.append(wallet.get().getId().toString())
                    .append(wallet.get().getBalance().toString())
                    .append(wallet.get().getLastUpdated().toString());
        }

        return UUID.nameUUIDFromBytes(builder.toString().getBytes()).toString()
                .equalsIgnoreCase(request.getGlobalId());
    }

    private boolean error(ConstraintValidatorContext context, String message) {

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}