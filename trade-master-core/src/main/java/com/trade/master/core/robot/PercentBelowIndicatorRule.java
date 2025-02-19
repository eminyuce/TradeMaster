package com.trade.master.core.robot;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.trading.rules.AbstractRule;

/**
 * Created by habanoz on 30.07.2017.
 */
public class PercentBelowIndicatorRule extends AbstractRule {

    private final Indicator<Decimal> first;
    private final Indicator<Decimal> second;
    private double percent;

    public PercentBelowIndicatorRule(Indicator<Decimal> first, Indicator<Decimal> second, double percent) {
        this.first = first;
        this.second = second;
        this.percent = percent;
    }

    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        return this.first.getValue(index).isLessThan(this.second.getValue(index).multipliedBy(Decimal.valueOf(1 - percent / 100)));
    }
}
