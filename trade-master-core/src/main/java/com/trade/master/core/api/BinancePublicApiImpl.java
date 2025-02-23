package com.trade.master.core.api;

import com.trade.master.core.model.PoloniexChart;
import com.trade.master.core.model.PoloniexTicker;
import com.trade.master.core.model.PoloniexTrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Poloniex Trading API client rewritten to use Java 17 HttpClient.
 *
 * @author David
 */
@Slf4j
@Component
public class BinancePublicApiImpl implements BinancePublicApi {

    private static final String PUBLIC_URL = "https://poloniex.com/public?";
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    public BinancePublicApiImpl() {


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
