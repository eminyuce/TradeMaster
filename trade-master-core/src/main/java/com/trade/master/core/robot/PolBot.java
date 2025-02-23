package com.trade.master.core.robot;

import com.trade.master.core.api.BinanceTradingApi;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huseyina on 5/29/2017.
 */
public interface PolBot {
    String BUY_ACTION = "BUY";
    String SELL_ACTION = "SELL";

    void execute();

    void startTradingForEachUser(BotUser user, Map<String, PoloniexTicker> tickerMap);

    List<CurrencyConfig> getCurrencyConfigs(BotUser user);

    void sendNotificationMail(BotUser user, Map<String, PoloniexCompleteBalance> completeBalanceMap, Map<String, List<PoloniexTrade>> recentHistoryMap, List<PoloniexOrderResult> orderResults);

    void cancelOrders(BinanceTradingApi tradingApi, List<PoloniexOpenOrder> openOrderList, PolStrategy patienceStrategy, Date now);

    BigDecimal createOrders(BotUser user, BinanceTradingApi tradingApi, BigDecimal btcBalance, List<PoloniexOrderResult> orderResults, List<Order> orders);
}
