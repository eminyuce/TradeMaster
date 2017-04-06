package com.habanoz.polbot.core.api;

import com.cf.TradingAPIClient;
import com.cf.client.poloniex.PoloniexExchangeService;
import com.cf.client.poloniex.PoloniexTradingAPIClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.habanoz.polbot.core.model.PoloniexCompleteBalance;
import com.habanoz.polbot.core.model.PoloniexOpenOrder;
import com.habanoz.polbot.core.model.PoloniexTradeHistory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by habanoz on 02.04.2017.
 */
@Component
public class PoloniexTradingApiImpl implements PoloniexTradingApi {
    private static final Logger logger = LoggerFactory.getLogger(PoloniexTradingApiImpl.class);
    private TradingAPIClient tradingAPIClient;
    private ObjectMapper objectMapper;

    public PoloniexTradingApiImpl(@Value("${api}") String apiKey, @Value("${secret}") String secretKey) {
        tradingAPIClient = new PoloniexTradingAPIClient(apiKey, secretKey);

        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Override
    public Map runCommand(String commandName, List<NameValuePair> params, TypeReference typeReference) {
        try {
            return objectMapper.readValue(tradingAPIClient.returnTradingAPICommandResults(commandName, params), typeReference);
        } catch (IOException e) {
            logger.error("Error while running command {}", commandName, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public String buy(String currencyPair, BigDecimal buyPrice, BigDecimal amount) {
        return tradingAPIClient.buy(currencyPair, buyPrice, amount, false, false, false);
    }

    @Override
    public String sell(String currencyPair, BigDecimal sellPrice, BigDecimal amount) {
        return tradingAPIClient.sell(currencyPair, sellPrice, amount, false, false, false);
    }

    @Override
    public Map runCommand(String commandName, TypeReference typeReference) {
        return runCommand(commandName, Collections.EMPTY_LIST, typeReference);
    }

    @Override
    public Map<String, List<PoloniexOpenOrder>> returnOpenOrders() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("currencyPair", "all"));
        return runCommand("returnOpenOrders", params, new TypeReference<HashMap<String, List<PoloniexOpenOrder>>>() {
        });
    }

    @Override
    public Map<String, Float> returnBalances() {
        return runCommand("returnBalances", new TypeReference<HashMap<String, Float>>() {
        });
    }

    @Override
    public Map<String, PoloniexCompleteBalance> returnCompleteBalances() {
        return runCommand("returnCompleteBalances", new TypeReference<HashMap<String, PoloniexCompleteBalance>>() {
        });
    }

    @Override
    public Float returnBalance(String cur) {
        return returnBalances().get(cur);
    }

    @Override
    public Map<String, List<PoloniexTradeHistory>> returnTradeHistory() {


        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("currencyPair", "all"));
        additionalPostParams.add(new BasicNameValuePair("start", PoloniexExchangeService.LONG_LONG_AGO.toString()));
        //return runCommand("returnTradeHistory", additionalPostParams, new TypeReference<HashMap<String, List<PoloniexTradeHistory>>>() {
        // });

        try {
            return objectMapper.readValue(tradingAPIClient.returnTradingAPICommandResults("returnTradeHistory", additionalPostParams), new TypeReference<HashMap<String, List<PoloniexTradeHistory>>>() {
            });
        } catch (IOException e) {
            logger.error("Error while running command {}", "returnTradeHistory", e);
            return Collections.emptyMap();
        }
    }
}
