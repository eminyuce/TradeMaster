package com.stock.ticker;


import com.stock.ticker.data.model.poloniex.PoloniexChartData;
import com.stock.ticker.data.model.poloniex.PoloniexCompleteBalance;
import com.stock.ticker.data.model.poloniex.PoloniexFeeInfo;
import com.stock.ticker.data.model.poloniex.PoloniexOpenOrder;
import com.stock.ticker.data.model.poloniex.PoloniexOrderResult;
import com.stock.ticker.data.model.poloniex.PoloniexTicker;
import com.stock.ticker.data.model.poloniex.PoloniexTradeHistory;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author David
 */
public interface ExchangeService
{
    String USDT_BTC_CURRENCY_PAIR = "USDT_BTC";
    String USDT_ETH_CURRENCY_PAIR = "USDT_ETH";
    String BTC_CURRENCY_TYPE = "BTC";
    String ETH_CURRENCY_TYPE = "ETH";
    Long FIFTEEN_MINUTES_TIME_PERIOD = 900L;
    Long FOUR_HOUR_TIME_PERIOD = 14_400L;
    Long TWO_HOUR_TIME_PERIOD = 7_200L;
    Long DAILY_TIME_PERIOD = 86_400L;
    Long LONG_LONG_AGO = 1_439_000_000L;

    List<PoloniexChartData> returnBTCChartData(Long periodInSeconds, Long startEpochInSeconds);
    
    List<PoloniexChartData> returnETHChartData(Long periodInSeconds, Long startEpochInSeconds);
    
    PoloniexTicker returnTicker(String currencyName);

    PoloniexCompleteBalance returnBalance(String currencyName);

    PoloniexFeeInfo returnFeeInfo();
    
    List<PoloniexOpenOrder> returnOpenOrders(String currencyName);
    
    List<PoloniexTradeHistory> returnTradeHistory(String currencyPair);
    
    boolean cancelOrder(String orderNumber);
    
    PoloniexOrderResult sell(String currencyPair, BigDecimal sellPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly);

    PoloniexOrderResult buy(String currencyPair, BigDecimal buyPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly);

}
