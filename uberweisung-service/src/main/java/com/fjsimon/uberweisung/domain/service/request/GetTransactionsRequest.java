package com.fjsimon.uberweisung.domain.service.request;

import com.fjsimon.uberweisung.validation.CheckOwnershipGet;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CheckOwnershipGet(message = "{wallet.owner.invalid}")
public class GetTransactionsRequest {

    @NotNull
    public Integer walletId;

    @NotNull
    public String username;

}
