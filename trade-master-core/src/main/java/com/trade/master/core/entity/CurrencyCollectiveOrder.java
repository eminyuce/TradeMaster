package com.trade.master.core.entity;

import lombok.*;

/**
 * Created by Yuce on 5/12/2017.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CurrencyCollectiveOrder {
    private String currencyPair;
    private String orderType;
    private String topPriceStr = "0";
    private String bottomPriceStr = "0";
    private int priceSplitter;
    private double totalBtcAmount;
}