package com.fjsimon.uberweisung.validation.validator;

import com.fjsimon.uberweisung.repository.UserRepository;
import com.fjsimon.uberweisung.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckOwnershipGetValidatorTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CheckOwnershipGetValidator testee;

    @Test
    public void isValid_test() {


    }
}