package com.fjsimon.rates.client;

import com.fjsimon.rates.model.ReferenceRatesResponse;

import java.util.Optional;

public interface RatesClient {

    Optional<ReferenceRatesResponse> retrieveRates();
}
