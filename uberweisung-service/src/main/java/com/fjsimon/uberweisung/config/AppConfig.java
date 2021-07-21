package com.fjsimon.uberweisung.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Configuration
@ComponentScan({"com.fjsimon.uberweisung", "com.fjsimon.rates"})
public class AppConfig {

    @Value("${app.file.path}")
    private String filePath;

    @Bean
    public Table<String, String, BigDecimal> referenceRates() {

        CSVReader reader;
        ImmutableTable.Builder<String, String, BigDecimal> builder = ImmutableTable.builder();
        try {
            String path = ResourceUtils.getFile(filePath).getAbsolutePath();
            reader = new CSVReader(new FileReader(path));
            String[] currencies = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                String date = line[0];
                for (int i = 1; i < line.length - 1; i++) {
                    BigDecimal value = "N/A".equals(line[i]) ? BigDecimal.ZERO : new BigDecimal(line[i]);
                    builder.put(date, currencies[i], value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }
}
