package com.trade.master.core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.stock.ticker.client.StockHttpClient;
import com.trade.master.core.model.PoloniexChart;
import com.trade.master.core.model.PoloniexTicker;
import com.trade.master.core.model.PoloniexTrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Poloniex Trading API client rewritten to use Java 17 HttpClient.
 *
 * @author David
 */
@Slf4j
@Component
public class PoloniexPublicApiImpl implements PoloniexPublicApi {

    private static final String PUBLIC_URL = "https://poloniex.com/public?";
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    public PoloniexPublicApiImpl() {


    }

    @Override
    public Map<String, PoloniexTicker> returnTicker() {
        return Collections.emptyMap();
    }

    @Override
    public List<PoloniexTrade> returnTradeHistory(String currencyPair, long start, long end) {
        return Collections.EMPTY_LIST;
    }


    @Override
    public List<PoloniexChart> returnChart(String currencyPair, Long periodInSeconds, Long startTime, Long endTime) {
        return Collections.EMPTY_LIST;
    }
}
