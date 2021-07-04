package com.fjsimon.uberweisung.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fjsimon.uberweisung.domain.service.response.ReferenceRatesResponse;
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
import org.springframework.web.client.RestTemplate;

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
    public BigDecimal convert(@DateFormat @RequestParam(value="date") String date,
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
                .multiply(new BigDecimal(amount));
    }

    // 3) Given a start Date, an end Date and a Currency, return the highest reference
    // exchange rate that the Currency achieved for the period.
    @GetMapping(value = "/maximum", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double max(@DateFormat @RequestParam(value="start") String start,
                      @DateFormat @RequestParam(value="end") String end,
                      @CurrencyCheck @RequestParam(value="currency") String currency) {

        LOGGER.info(String.format("GET /reference/rates/maximum?start=%s&end=%s&currency=%s",
                start, end, currency));

        Map<String, BigDecimal> map = referenceRates.column(currency);
        return map.entrySet().stream()
                .filter(e -> isBetweenDates(e.getKey(), start, end))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .max()
                .orElse(0.0);
    }

    // 4) Given a start Date, an end Date and a Currency, determine and return the average
    // reference exchange rate of that Currency for the period.
    @GetMapping(value = "/average", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double avg(@DateFormat @RequestParam(value="start") String start,
                      @DateFormat @RequestParam(value="end") String end,
                      @CurrencyCheck @RequestParam(value="currency") String currency) {

        LOGGER.info(String.format("GET /reference/rates/average?start=%s&end=%s&currency=%s",
                start, end, currency));

        Map<String, BigDecimal> map = referenceRates.column(currency);
        return map.entrySet().stream()
                .filter(e -> isBetweenDates(e.getKey(), start, end))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .average()
                .orElse(0.0);
    }


    @GetMapping(value = "/now", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReferenceRatesResponse referenceRates() throws JsonProcessingException {

        LOGGER.info(String.format("GET /reference/rates/now"));

        RestTemplate restTemplate = new RestTemplate();
        final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        String response = restTemplate.getForObject(url, String.class);

        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        xmlMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);


        return xmlMapper.readValue(response, ReferenceRatesResponse.class);
    }

    public boolean isBetweenDates(String date, String start, String end) {

        LocalDate selectedDate = LocalDate.parse(date);
        return selectedDate.isAfter(LocalDate.parse(start).minusDays(1)) &&
                selectedDate.isBefore(LocalDate.parse(end).plusDays(1));
    }
}

