package com.trade.master.core.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author David
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexChart {
    private BigDecimal date;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private BigDecimal weightedAverage;

    public PoloniexChart(BigDecimal high, BigDecimal low, BigDecimal open, BigDecimal close, BigDecimal volume) {
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

    public PoloniexChart(BigDecimal date, BigDecimal high, BigDecimal low, BigDecimal open, BigDecimal close, BigDecimal volume) {
        this.date = date;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

}
