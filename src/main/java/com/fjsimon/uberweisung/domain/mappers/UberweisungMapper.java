package com.fjsimon.uberweisung.domain.mappers;

import com.fjsimon.uberweisung.domain.repository.TRANSACTION_TYPE;
import com.fjsimon.uberweisung.domain.repository.Transaction;
import com.fjsimon.uberweisung.domain.repository.User;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UberweisungMapper {

    static String transactionTypeMapper(TRANSACTION_TYPE type) {

        return type.name();
    }

    UserResponse map(User user);

    @Mapping(target = "transactionType", source = "transaction_type", qualifiedByName = "transactionTypeMapper")
    GetTransactionResponse mapTransaction(Transaction transaction);

    List<GetTransactionResponse> mapTransactions(List<Transaction> transactions);
}
