package com.trade.master.core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.trade.master.core.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by habanoz on 02.04.2017.
 */
public interface PoloniexTradingApi {
    PoloniexOrderResult buy(Order order);

    PoloniexOrderResult sell(Order order);

    Map runCommand(String commandName, TypeReference typeReference);

    Map runCommand(String commandName, List<org.apache.hc.core5.http.NameValuePair> params, TypeReference typeReference);

    Map<String, List<PoloniexOpenOrder>> returnOpenOrders();

    Map<String, BigDecimal> returnBalances();

    Map<String, PoloniexCompleteBalance> returnCompleteBalances();

    BigDecimal returnBalance(String cur);

    Map<String, List<PoloniexTrade>> returnTradeHistory();

    Map<String, List<PoloniexTrade>> returnTradeHistory(Long start);

    boolean cancelOrder(String orderNumber);
}
