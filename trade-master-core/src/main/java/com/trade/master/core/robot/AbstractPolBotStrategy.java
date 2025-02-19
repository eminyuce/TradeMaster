package com.trade.master.core.robot;

import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.model.PoloniexChart;
import com.trade.master.core.model.PoloniexOpenOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by habanoz on 31.07.2017.
 */
public abstract class AbstractPolBotStrategy implements PolStrategy {

    protected static final double minAmount = 0.0001;
    protected static final String CURR_PAIR_SEPARATOR = "_";
    protected final CurrencyConfig currencyConfig;
    protected final List<PoloniexChart> chartData;
    protected final int timeFrame;

    public AbstractPolBotStrategy(CurrencyConfig currencyConfig, List<PoloniexChart> chartData, int timeFrame) {
        this.currencyConfig = currencyConfig;
        this.chartData = chartData;
        this.timeFrame = timeFrame;
    }

    @Override
    public List<PoloniexOpenOrder> getOrdersToCancel(List<PoloniexOpenOrder> openOrderList) {
        return getOrdersToCancel(openOrderList, new Date());
    }

    @Override
    public List<PoloniexOpenOrder> getOrdersToCancel(List<PoloniexOpenOrder> openOrderList, Date date) {

        Iterator<PoloniexOpenOrder> openOrderIterator = openOrderList.iterator();
        List<PoloniexOpenOrder> openOrdersToCancel = new ArrayList<>();
        while (openOrderIterator.hasNext()) {
            PoloniexOpenOrder openOrder = openOrderIterator.next();
            if (currencyConfig.getOrderTimeoutInHour() > 0 &&
                    (date.getTime() - openOrder.getDate().getTime()) > currencyConfig.getOrderTimeoutInHour() * 1000 * 60 * 60) {
                openOrdersToCancel.add(openOrder);
            }
        }

        return openOrdersToCancel;
    }


}
