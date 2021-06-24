package com.fjsimon.uberweisung.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReferenceRatesControllerTest {

    @Autowired
    private ReferenceRatesController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;

    @Test
    public void references() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates?date={date}",
                HttpMethod.GET, entity, HashMap.class, "2020-09-10");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().get("USD").toString(), is("1.1849"));
        assertThat(restResponse.getBody().get("JPY").toString(), is("125.76"));
        assertThat(restResponse.getBody().get("DKK").toString(), is("7.4404"));
        assertThat(restResponse.getBody().get("GBP").toString(), is("0.9159"));
    }

    @Test
    public void convert() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/convert" +
                        "?date={date}&source={source}&target={target}&amount={amount}",
                HttpMethod.GET, entity, String.class, "2020-09-10", "JPY", "DKK", "100.0");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().toString(), is("5.916348600508905852417302798982188000"));
    }

    @Test
    public void maximum() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/maximum" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "2020-09-10", "2020-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().toString(), is("7.4404"));
    }

    @Test
    public void average() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/average" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "2020-09-10", "2020-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().toString(), is("7.440133333333333"));
    }

    @Test
    public void average_old_dates() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/average" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "1900-09-10", "1900-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().toString(), is("0.0"));
    }

    @Test
    public void validation_rates_empty() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates?date={date}",
                HttpMethod.GET, entity, HashMap.class, "");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }

    @Test
    public void validation_rates_bad_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates?date={date}",
                HttpMethod.GET, entity, HashMap.class, "wrong");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }


//        this.mockMvc.perform(get("/reference/rates/convert")
//                .param("date", "wrong")
//                .param("source", "JPY")
//                .param("target", "DKK")
//                .param("amount", "100.0"))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").hasJsonPath())
//                .andExpect(jsonPath("$").value("convert.date: date format invalid [yyyy-MM-dd]"));
//
//        this.mockMvc.perform(get("/reference/rates/convert")
//                .param("date", "2020-09-14")
//                .param("source", "BLA")
//                .param("target", "DKK")
//                .param("amount", "100.0"))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").hasJsonPath())
//                .andExpect(jsonPath("$").value("convert.source: currency [BLA] is invalid"));
//
//        this.mockMvc.perform(get("/reference/rates/convert")
//                .param("date", "2020-09-14")
//                .param("source", "JPY")
//                .param("target", "BLO")
//                .param("amount", "100.0"))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").hasJsonPath())
//                .andExpect(jsonPath("$").value("convert.target: currency [BLO] is invalid"));
//
//        this.mockMvc.perform(get("/reference/rates/convert")
//                .param("date", "2020-09-14")
//                .param("source", "JPY")
//                .param("target", "DKK")
//                .param("amount", "WRONG"))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").hasJsonPath())
//                .andExpect(jsonPath("$").value("convert.amount: amount format invalid"));

    @Test
    public void isBetweenDatesTest() {

        assertThat(controller.isBetweenDates("2020-09-14", "2020-09-14", "2020-09-15"), is(true));
        assertThat(controller.isBetweenDates("2020-09-15", "2020-09-14", "2020-09-15"), is(true));
        assertThat(controller.isBetweenDates("2020-09-15", "2020-09-14", "2020-09-15"), is(true));

        assertThat(controller.isBetweenDates("2020-09-13", "2020-09-14", "2020-09-15"), is(false));
        assertThat(controller.isBetweenDates("2019-09-14", "2020-09-14", "2020-09-15"), is(false));
        assertThat(controller.isBetweenDates("2020-10-14", "2020-09-14", "2020-09-15"), is(false));

    }
}