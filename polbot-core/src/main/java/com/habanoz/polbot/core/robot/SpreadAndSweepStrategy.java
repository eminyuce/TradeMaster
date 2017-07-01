package com.habanoz.polbot.core.robot;

import com.habanoz.polbot.core.entity.CurrencyConfig;
import com.habanoz.polbot.core.model.Order;
import com.habanoz.polbot.core.model.PoloniexOpenOrder;
import com.habanoz.polbot.core.model.PoloniexTrade;
import com.habanoz.polbot.core.utils.ExchangePrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Trading bot with Buy when cheap sell when high logic
 * <p>
 * Created by habanoz on 05.04.2017.
 */
public class SpreadAndSweepStrategy implements PolStrategy {
    private static final Logger logger = LoggerFactory.getLogger(PoloniexTrade.class);

    private static final double minAmount = 0.0001;
    private static final String CURR_PAIR_SEPARATOR = "_";
    private List<PoloniexOpenOrder> openOrderList;
    private List<PoloniexTrade> historyList;

    public SpreadAndSweepStrategy(List<PoloniexOpenOrder> openOrderList, List<PoloniexTrade> historyList) {
        this.openOrderList = openOrderList;
        this.historyList = historyList;
    }


    @Override
    public List<Order> execute(CurrencyConfig currencyConfig, ExchangePrice priceData, BigDecimal btcBalance, BigDecimal coinBalance, Date date) {
        String currPair = currencyConfig.getCurrencyPair();

        // this may indicate invalid currency name
        if (priceData == null)
            return Collections.emptyList();

        return runStrategy(currencyConfig, currPair, btcBalance, coinBalance, openOrderList, priceData, date);
    }

    private List<Order> runStrategy(CurrencyConfig currencyConfig, String currPair, BigDecimal btcBalance, BigDecimal coinBalance, List<PoloniexOpenOrder> openOrderListForCurr, ExchangePrice priceData, Date date) {
        List<Order> poloniexOrders = new ArrayList<>();

        //current lowest market price
        BigDecimal lowestBuyPrice = priceData.getBuyPrice();
        BigDecimal highestSellPrice = priceData.getSellPrice();


        //
        //
        // buy logic
        if (currencyConfig.getUsableBalancePercent() > 0 &&
                currencyConfig.getBuyable() &&
                openOrderListForCurr.stream().noneMatch(r -> r.getType().equalsIgnoreCase(PolBot.BUY_ACTION))) {

            Order order = createBuyOrder(currencyConfig, currPair, lowestBuyPrice, btcBalance, date);

            if (order != null)
                poloniexOrders.add(order);

        }

        //
        //
        // sell logic
        if (currencyConfig.getSellable() && coinBalance.doubleValue() > minAmount) {
            Order openOrder = createSellOrder(currencyConfig, currPair, coinBalance, highestSellPrice, historyList, date);
            if (openOrder != null)
                poloniexOrders.add(openOrder);
        }

        return poloniexOrders;
    }

    private Order createSellOrder(CurrencyConfig currencyConfig,
                                              String currPair,
                                              BigDecimal currCoinAmount,
                                              BigDecimal highestSellPrice,
                                              List<PoloniexTrade> currHistoryList, Date date) {

        // get last buying price to calculate selling price
        BigDecimal lastBuyPrice = getBuyPrice(highestSellPrice, currHistoryList);

        //selling price should be a little higher to make profit
        // if set, sell at price will be used, otherwise sell on percent will be used
        BigDecimal sellPrice = currencyConfig.getSellAtPrice() == 0 ? lastBuyPrice.multiply(new BigDecimal(1).add(BigDecimal.valueOf(currencyConfig.getSellOnPercent() * 0.01))) : new BigDecimal(currencyConfig.getSellAtPrice());

        return new Order(currPair, "SELL", sellPrice, currCoinAmount, date);
    }

    private BigDecimal getBuyPrice(BigDecimal highestSellPrice, List<PoloniexTrade> currHistoryList) {
        BigDecimal lastBuyPrice = highestSellPrice;

        if (currHistoryList != null && currHistoryList.size() > 0 && currHistoryList.get(0) != null) {
            for (int i = currHistoryList.size() - 1; currHistoryList.size() >= 0; i--) {
                PoloniexTrade history = currHistoryList.get(i);

                // if remaining history records are too old, dont use them for selling price base
                if (history.getDate().plus(1, ChronoUnit.WEEKS).isBefore(LocalDateTime.now()))
                    break;

                // use most recent buy action as sell base
                if (history.getType().equalsIgnoreCase("buy")) {
                    lastBuyPrice = history.getRate();
                    break;
                }
            }
        }

        return lastBuyPrice;
    }

    private Order createBuyOrder(CurrencyConfig currencyConfig,
                                             String currPair,
                                             BigDecimal lowestBuyPrice, BigDecimal buyBudgetInBtc, Date date) {
        // not enough budget, return 0
        if (buyBudgetInBtc == null || buyBudgetInBtc.doubleValue() < minAmount) {
            return null;
        }

        // buying price should be a little lower to make profit
        // if set, buy at price will be used, other wise buy on percent will be used
        BigDecimal buyPrice = lowestBuyPrice.multiply(new BigDecimal(1).subtract(BigDecimal.valueOf(currencyConfig.getBuyOnPercent() * 0.01)));

        BigDecimal splitCount = BigDecimal.valueOf(currencyConfig.getBuyAtPrice());

        // calculate amount that can be bought with buyBudget and buyPrice
        BigDecimal buyCoinAmount = buyBudgetInBtc.divide(splitCount, RoundingMode.DOWN).divide(buyPrice, RoundingMode.DOWN);

        return new Order(currPair, PoloniexPatienceBot.BUY_ACTION, buyPrice, buyCoinAmount, date);
    }

    @Override
    public List<PoloniexOpenOrder> getOrdersToCancel(CurrencyConfig currencyConfig, Date date) {

        Iterator<PoloniexOpenOrder> openOrderIterator = openOrderList.iterator();
        List<PoloniexOpenOrder> openOrdersToCancel = new ArrayList<>();
        while (openOrderIterator.hasNext()) {
            PoloniexOpenOrder openOrder = openOrderIterator.next();
            if (currencyConfig.getBuyOrderCancellationHour() > 0 &&
                    (date.getTime() - openOrder.getDate().getTime()) > currencyConfig.getBuyOrderCancellationHour() * 1000 * 60 * 60) {
                openOrdersToCancel.add(openOrder);
            }
        }

        return openOrdersToCancel;
    }
}
