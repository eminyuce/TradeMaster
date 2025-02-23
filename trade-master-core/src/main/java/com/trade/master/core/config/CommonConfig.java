package com.trade.master.core.config;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Configuration
public class CommonConfig {
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.secret.key}")
    private String secretKey;

    @Bean
    public SpotClient binanceClient() {
        return new SpotClientImpl(apiKey, secretKey);
    }
    @Bean
    public ObjectMapper objectMapper() {

        var module = new JavaTimeModule();
        var localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT_STR));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        var objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR));
        return objectMapper;
    }
}
