package com.trade.master.core.model;

import lombok.*;

/**
 * Created by habanoz on 05.04.2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AnalysisConfig {
    private String currencyPair;
    private float buyAtPrice = 0;
    private float buyOnPercent;
    private float sellAtPrice = 0;
    private float sellOnPercent;
    private float orderTimeoutInHour = 0;
    private int startDaysAgo;
    private long periodInSec;
    private String botName;

    public AnalysisConfig(String currencyPair, float buyAtPrice, float buyOnPercent, float sellAtPrice, float sellOnPercent, int startDaysAgo, long periodInSec, String botName) {
        this.currencyPair = currencyPair;
        this.buyAtPrice = buyAtPrice;
        this.buyOnPercent = buyOnPercent;
        this.sellAtPrice = sellAtPrice;
        this.sellOnPercent = sellOnPercent;
        this.startDaysAgo = startDaysAgo;
        this.periodInSec = periodInSec;
        this.botName = botName;
    }

}
