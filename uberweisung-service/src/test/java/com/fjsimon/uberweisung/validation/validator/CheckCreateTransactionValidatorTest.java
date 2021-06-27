package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.repository.Wallet;
import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.repository.TransactionRepository;
import com.fjsimon.uberweisung.repository.UserRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckCreateTransactionValidatorTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CheckCreateTransactionValidator testee;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Test
    public void isValid_debit_returns_wallet_owner_invalid() {

        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);

        assertThat(testee.isValid(new CreateTransactionRequest(), constraintValidatorContext), is(false));

        verify(constraintValidatorContext, times(1)).disableDefaultConstraintViolation();
        verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate(messageCaptor.capture());

        assertThat(messageCaptor.getValue(), is("{wallet.owner.invalid}"));
    }

    @Test
    public void isValid_debit_returns_operation_not_permitted() {

        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal(0));
        User user = new User();
        user.setId(1L);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(wallet));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(1);
        request.setUsername("username");
        request.setAmount(new BigDecimal(10));

        assertThat(testee.isValid(request, constraintValidatorContext), is(false));

        verify(constraintValidatorContext, times(1)).disableDefaultConstraintViolation();
        verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate(messageCaptor.capture());

        assertThat(messageCaptor.getValue(), is("{operation.not.permitted}"));
    }

    @Test
    public void isValid_debit_returns_true() {

        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal(1000));
        User user = new User();
        user.setId(1L);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(wallet));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(1);
        request.setUsername("username");
        request.setAmount(new BigDecimal(10));
        request.setTransactionType(TRANSACTION_TYPE.D.name());

        assertThat(testee.isValid(request, constraintValidatorContext), is(true));

        verifyZeroInteractions(constraintValidatorContext);
    }

    @Test
    public void isValid_debit_zero_balance_returns_true() {

        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal(1000));
        User user = new User();
        user.setId(1L);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(wallet));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(1);
        request.setUsername("username");
        request.setAmount(new BigDecimal(1000));
        request.setTransactionType(TRANSACTION_TYPE.D.name());

        assertThat(testee.isValid(request, constraintValidatorContext), is(true));

        verifyZeroInteractions(constraintValidatorContext);
    }

    @Test
    public void isValid_credit_returns_true() {

        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal(0));
        User user = new User();
        user.setId(1L);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(wallet));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(1);
        request.setUsername("username");
        request.setAmount(new BigDecimal(10));
        request.setTransactionType(TRANSACTION_TYPE.C.name());

        assertThat(testee.isValid(request, constraintValidatorContext), is(true));

        verifyZeroInteractions(constraintValidatorContext);
    }
}