package com.fjsimon.uberweisung.controller;

import com.fjsimon.uberweisung.domain.service.request.CreateTransactionRequest;
import com.fjsimon.uberweisung.domain.service.request.GetTransactionsRequest;
import com.fjsimon.uberweisung.domain.service.response.CreateTransactionResponse;
import com.fjsimon.uberweisung.domain.service.response.GetTransactionResponse;
import com.fjsimon.uberweisung.service.UberweisungService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/uberweisung")
@RequiredArgsConstructor
public class UberweisungController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UberweisungController.class);

    private final UberweisungService uberweisungService;

    @GetMapping(
            value = "/account/{walletId}/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public List<GetTransactionResponse> getTransactionsByWalletId(@PathVariable("walletId") int walletId, Principal principal) {

        LOGGER.info("getTransactionsByWalletId");

        GetTransactionsRequest getTransactionsRequest = GetTransactionsRequest.
                builder().username(principal.getName()).walletId(walletId).build();

        return uberweisungService.getTransactions(getTransactionsRequest);
    }

    @GetMapping(
            value = "/account/globalId",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public String getGlobalId(Principal principal) {

        LOGGER.info("getGlobalId");

        return uberweisungService.generateGlobalId();
    }

    @PostMapping(
            value = "/account/{walletId}/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public CreateTransactionResponse createWalletTransaction(@PathVariable("walletId") int walletId,
                                                             @RequestBody CreateTransactionRequest createTransactionRequest) {

        LOGGER.info("createWalletTransaction");

        createTransactionRequest.setWalletId(walletId);
        return uberweisungService.createTransaction(createTransactionRequest);
    }

}
