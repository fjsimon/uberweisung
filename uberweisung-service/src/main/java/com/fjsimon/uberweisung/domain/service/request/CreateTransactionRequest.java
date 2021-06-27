package com.fjsimon.uberweisung.domain.service.request;
import com.fjsimon.uberweisung.validation.CheckCreateTransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CheckCreateTransaction
public class CreateTransactionRequest {

    @NotNull
    public Integer walletId;

    @NotNull
    public String username;

    @NotNull
    private String globalId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String transactionType;

    protected String description;

}
