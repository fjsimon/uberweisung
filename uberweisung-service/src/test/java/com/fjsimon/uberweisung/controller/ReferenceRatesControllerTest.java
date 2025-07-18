package com.fjsimon.uberweisung.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fjsimon.rates.model.ReferenceRatesResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
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
        assertThat(restResponse.getBody(), is("5.916348600508905852417302798982188000"));
    }

    @Test
    public void maximum() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/maximum" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "2020-09-10", "2020-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody(), is("7.4404"));
    }

    @Test
    public void average() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/average" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "2020-09-10", "2020-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody(), is("7.440133333333333"));
    }

    @Test
    public void average_old_dates() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<String> restResponse = restTemplate.exchange("/reference/rates/average" +
                        "?start={start}&end={end}&currency={currency}",
                HttpMethod.GET, entity, String.class, "1900-09-10", "1900-09-14", "DKK");

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody(), is("0.0"));
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

    @Test
    public void validation_convert_date_bad_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates/convert" +
                        "?date={date}&source={source}&target={target}&amount={amount}",
                HttpMethod.GET, entity, HashMap.class, "wrong", "JPY", "DKK", "100.0");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }

    @Test
    public void validation_convert_source_bad_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates/convert" +
                        "?date={date}&source={source}&target={target}&amount={amount}",
                HttpMethod.GET, entity, HashMap.class, "2020-09-14", "BLA", "DKK", "100.0");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }

    @Test
    public void validation_convert_target_bad_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates/convert" +
                        "?date={date}&source={source}&target={target}&amount={amount}",
                HttpMethod.GET, entity, HashMap.class, "2020-09-14", "JPY", "BLO", "100.0");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }

    @Test
    public void validation_convert_amount_bad_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<HashMap> restResponse = restTemplate.exchange("/reference/rates/convert" +
                        "?date={date}&source={source}&target={target}&amount={amount}",
                HttpMethod.GET, entity, HashMap.class, "2020-09-14", "JPY", "DKK", "wrong");

        assertThat(restResponse.getStatusCode().toString(), is("400 BAD_REQUEST"));
    }

    @Test
    public void rates_daily_request() throws Exception {

        HttpEntity entity = new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN"));

        ResponseEntity<ReferenceRatesResponse> restResponse = restTemplate.exchange("/reference/rates/daily",
                HttpMethod.GET, entity, ReferenceRatesResponse.class);

        assertThat(restResponse.getStatusCode().toString(), is("200 OK"));
        assertThat(restResponse.getBody().getSubject(), is("Reference rates"));
        assertThat(restResponse.getBody().getSender().getName(), is("European Central Bank"));
        assertThat(restResponse.getBody().getCube().getCube().size(), is(30));
    }

    @Test
    public void isBetweenDatesTest() {

        assertThat(controller.isBetweenDates("2020-09-14", "2020-09-14", "2020-09-15"), is(true));
        assertThat(controller.isBetweenDates("2020-09-15", "2020-09-14", "2020-09-15"), is(true));
        assertThat(controller.isBetweenDates("2020-09-15", "2020-09-14", "2020-09-15"), is(true));

        assertThat(controller.isBetweenDates("2020-09-13", "2020-09-14", "2020-09-15"), is(false));
        assertThat(controller.isBetweenDates("2019-09-14", "2020-09-14", "2020-09-15"), is(false));
        assertThat(controller.isBetweenDates("2020-10-14", "2020-09-14", "2020-09-15"), is(false));

    }

    @Test
    public void xmlMapperTest() throws JsonProcessingException {

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

        ReferenceRatesResponse referenceRatesResponse = xmlMapper.readValue(response, ReferenceRatesResponse.class);
        System.out.printf("Details : %n%s%n", referenceRatesResponse.toString());
    }

}