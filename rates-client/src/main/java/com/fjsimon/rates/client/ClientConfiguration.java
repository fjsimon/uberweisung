package com.fjsimon.rates.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.springframework.util.Assert.notNull;

@Configuration
@EnableAspectJAutoProxy
public class ClientConfiguration {

    public static final String PROPERTY_URL = ".http.url";
    public static final int TIMEOUT = 5000;

    @Autowired
    private Environment environment;

    @Bean
    @DependsOn("restTemplate")
    public String exchangeRatesBaseUri() {

        return getUrl("exchange.rates");
    }

    private String getUrl(String type) {

        notNull(environment, "the Environment must not be null");
        return environment.getProperty(type + PROPERTY_URL);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        builder.setConnectTimeout(Duration.ofMillis(TIMEOUT));
        builder.setReadTimeout(Duration.ofMillis(TIMEOUT));
        return builder.build();
    }

    @Bean
    public ObjectMapper objectMapper() {

        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.registerModule(new JavaTimeModule());

        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        xmlMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        xmlMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        xmlMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        return xmlMapper;
    }
}
