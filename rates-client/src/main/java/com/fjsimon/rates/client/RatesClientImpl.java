package com.fjsimon.rates.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fjsimon.rates.model.ReferenceRatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RatesClientImpl implements RatesClient {

    private static final Logger log = LoggerFactory.getLogger(RatesClientImpl.class);

    private static final TypeReference<ReferenceRatesResponse> REFERENCE_RATES_RESPONSE_TYPE = new TypeReference<ReferenceRatesResponse>() {};

    @Autowired
    private RatesServiceCaller caller;

    @Autowired
    private UrlBuilder urlBuilder;

    @Override
    public Optional<ReferenceRatesResponse> retrieveRates() {

        String url = urlBuilder.aLink(Urls.GET_RATES).build();

        log.info("retrieve rates " + url);

        return caller.get(url, REFERENCE_RATES_RESPONSE_TYPE);
    }
}
