package com.trade.master.core.robot;

import com.trade.master.core.model.Order;
import com.trade.master.core.model.PoloniexChart;
import com.trade.master.core.model.PoloniexOpenOrder;
import com.trade.master.core.model.PoloniexTrade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by habanoz on 30.05.2017.
 */
public interface PolStrategy {
    List<Order> execute(PoloniexChart chart, BigDecimal btcBalance, BigDecimal coinBalance, List<PoloniexOpenOrder> openOrderList, List<PoloniexTrade> tradeHistory, List<PoloniexTrade> recentTradeHistory);

    List<PoloniexOpenOrder> getOrdersToCancel(List<PoloniexOpenOrder> openOrderList);

    List<PoloniexOpenOrder> getOrdersToCancel(List<PoloniexOpenOrder> openOrderList, Date date);
}
