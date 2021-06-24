package com.fjsimon.uberweisung.config;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Configuration
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
                for(int i=1; i < line.length-1; i++) {
                    BigDecimal value = "N/A".equals(line[i]) ? BigDecimal.ZERO : new BigDecimal(line[i]);
                    builder.put(date, currencies[i], value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();
    }
}
