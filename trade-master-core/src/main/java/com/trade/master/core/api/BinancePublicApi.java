package com.trade.master.core.api;


import com.trade.master.core.model.PoloniexChart;
import com.trade.master.core.model.PoloniexTicker;
import com.trade.master.core.model.PoloniexTrade;

import java.util.List;
import java.util.Map;

/**
 * Created by habanoz on 02.04.2017.
 */
public interface BinancePublicApi {
    Map<String, PoloniexTicker> returnTicker();

    List<PoloniexTrade> returnTradeHistory(String currencyPair, long start, long end);

    List<PoloniexChart> returnChart(String currencyPair, Long periodInSeconds, Long startTime, Long endTime);
}
