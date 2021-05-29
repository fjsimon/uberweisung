package com.fjsimon.uberweisung.domain.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionResponse {

    private String globalId;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime lastUpdated;
}

