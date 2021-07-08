package com.fjsimon.uberweisung.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fjsimon.rates.client.RatesClient;
import com.fjsimon.rates.model.ReferenceRatesResponse;
import com.fjsimon.uberweisung.validation.BigDecimalFormat;
import com.fjsimon.uberweisung.validation.CurrencyCheck;
import com.fjsimon.uberweisung.validation.DateFormat;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;


@Validated
@RestController
@RequestMapping("/reference/rates")
public class ReferenceRatesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceRatesController.class);

    @Autowired
    private Table<String, String, BigDecimal> referenceRates;

    @Autowired
    private RatesClient ratesClient;

    // 1) Allows an API caller to retrieve the reference rate data for a given Date for all available Currencies.
    @GetMapping()
    public Map<String, BigDecimal> references(@DateFormat @RequestParam(value="date") String date) {

        LOGGER.info(String.format("GET /reference/rates?date=%s", date));

        return referenceRates.row(date);
    }

    // 2) Given a Date, source Currency (eg. JPY), target Currency (eg. GBP), and an
    // Amount, returns the Amount given converted from the first to the second Currency as
    // it would have been on that Date (assuming zero fees).
    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public String convert(@DateFormat @RequestParam(value="date") String date,
                              @CurrencyCheck @RequestParam(value="source") String source,
                              @CurrencyCheck @RequestParam(value="target") String target,
                              @BigDecimalFormat @RequestParam(value="amount") String amount) {

        LOGGER.info(String.format("GET /reference/rates/convert?date=%s&source=%s&target=%s&amount=%s",
                date, source, target, amount));

        Map<String, BigDecimal> map = referenceRates.row(date);
        BigDecimal targetValue = map.get(target);
        BigDecimal sourceValue = map.get(source);
        if(BigDecimal.ZERO.equals(sourceValue) || BigDecimal.ZERO.equals(targetValue)) {
            throw new ArithmeticException("Operation Not Applicable");
        }
        return targetValue.divide(sourceValue, MathContext.DECIMAL128)
                .multiply(new BigDecimal(amount))
                .toString();
    }

    // 3) Given a start Date, an end Date and a Currency, return the highest reference
    // exchange rate that the Currency achieved for the period.
    @GetMapping(value = "/maximum", produces = MediaType.APPLICATION_JSON_VALUE)
    public String max(@DateFormat @RequestParam(value="start") String start,
                      @DateFormat @RequestParam(value="end") String end,
                      @CurrencyCheck @RequestParam(value="currency") String currency) {

        LOGGER.info(String.format("GET /reference/rates/maximum?start=%s&end=%s&currency=%s",
                start, end, currency));

        Map<String, BigDecimal> map = referenceRates.column(currency);
        double maximum = map.entrySet().stream()
                .filter(e -> isBetweenDates(e.getKey(), start, end))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .max()
                .orElse(0.0);
        return String.valueOf(maximum);
    }

    // 4) Given a start Date, an end Date and a Currency, determine and return the average
    // reference exchange rate of that Currency for the period.
    @GetMapping(value = "/average", produces = MediaType.APPLICATION_JSON_VALUE)
    public String avg(@DateFormat @RequestParam(value="start") String start,
                      @DateFormat @RequestParam(value="end") String end,
                      @CurrencyCheck @RequestParam(value="currency") String currency) {

        LOGGER.info(String.format("GET /reference/rates/average?start=%s&end=%s&currency=%s",
                start, end, currency));

        Map<String, BigDecimal> map = referenceRates.column(currency);
        double avg = map.entrySet().stream()
                .filter(e -> isBetweenDates(e.getKey(), start, end))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .average()
                .orElse(0.0);
        return String.valueOf(avg);
    }


    @GetMapping(value = "/now", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReferenceRatesResponse referenceRates() throws JsonProcessingException {

        LOGGER.info(String.format("GET /reference/rates/now"));

        Optional<ReferenceRatesResponse> response = ratesClient.retrieveRates();

        return response.isPresent() ? response.get() : new ReferenceRatesResponse();
    }

    public boolean isBetweenDates(String date, String start, String end) {

        LocalDate selectedDate = LocalDate.parse(date);
        return selectedDate.isAfter(LocalDate.parse(start).minusDays(1)) &&
                selectedDate.isBefore(LocalDate.parse(end).plusDays(1));
    }
}

