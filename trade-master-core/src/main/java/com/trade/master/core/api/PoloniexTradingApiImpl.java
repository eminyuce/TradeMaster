package com.trade.master.core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.stock.ticker.TradingAPIClient;
import com.stock.ticker.client.poloniex.PoloniexExchangeService;
import com.stock.ticker.client.poloniex.PoloniexTradingAPIClient;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PoloniexTradingApiImpl implements PoloniexTradingApi {

    private BotUser botUser;

    public PoloniexTradingApiImpl(BotUser botUser) {
        this.botUser = botUser;
    }


    public BotUser getBotUser() {
        return botUser;
    }

    @Override
    public Map runCommand(String commandName, List<org.apache.hc.core5.http.NameValuePair> params, TypeReference typeReference) {
        return Collections.emptyMap();
    }

    @Override
    public PoloniexOrderResult buy(Order order) {
        return null;
    }


    @Override
    public PoloniexOrderResult sell(Order order) {
        return null;
    }

    @Override
    public Map runCommand(String commandName, TypeReference typeReference) {
        return runCommand(commandName, Collections.EMPTY_LIST, typeReference);
    }

    @Override
    public Map<String, List<PoloniexOpenOrder>> returnOpenOrders() {
        List<org.apache.hc.core5.http.NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("currencyPair", "all"));
        return runCommand("returnOpenOrders", params, new TypeReference<HashMap<String, List<PoloniexOpenOrder>>>() {
        });
    }

    @Override
    public Map<String, BigDecimal> returnBalances() {
        return runCommand("returnBalances", new TypeReference<HashMap<String, BigDecimal>>() {
        });
    }

    @Override
    public Map<String, PoloniexCompleteBalance> returnCompleteBalances() {
        Map<String, PoloniexCompleteBalance> completeBalanceMap = runCommand("returnCompleteBalances", new TypeReference<HashMap<String, PoloniexCompleteBalance>>() {
        });

        completeBalanceMap = completeBalanceMap.entrySet().stream().filter(map -> map.getValue().getBtcValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return completeBalanceMap;
    }

    @Override
    public BigDecimal returnBalance(String cur) {
        return returnBalances().get(cur);
    }

    @Override
    public Map<String, List<PoloniexTrade>> returnTradeHistory() {
        return returnTradeHistory(PoloniexExchangeService.LONG_LONG_AGO);
    }

    @Override
    public Map<String, List<PoloniexTrade>> returnTradeHistory(Long timeStampInSeconds) {
        return Collections.emptyMap();
    }

    @Override
    public boolean cancelOrder(String orderNumber) {
        return false;
    }
}
