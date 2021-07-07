package com.fjsimon.rates.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class RatesServiceCaller {

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    public <V, T extends V> Optional<V> get(String url, TypeReference<T> type, Object... urlVariables) {

        return execute(HttpMethod.GET, url, null, type, urlVariables);
    }

    private <V, T extends V> Optional<V> execute(HttpMethod method, String url, Object body, TypeReference<T> type, Object... urlVariables) {

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, buildRequest(body), String.class, urlVariables);
            return response.hasBody() ? Optional.ofNullable(parse(response, type)) : Optional.empty();
        } catch (RestClientException e) {
            throw e;
        } catch (IOException e) {
            throw new RestClientException("parse exception", e);
        }
    }

    private <V, T extends V> V parse(ResponseEntity<String> response, TypeReference<T> type) throws IOException {

        String body = new String(response.getBody().getBytes(), StandardCharsets.UTF_8);
        T value = mapper.readValue(body, type);
        return value;
    }

    private <T> HttpEntity<T> buildRequest(T body){

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(body, headers);
    }
}
