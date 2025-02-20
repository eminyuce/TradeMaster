package com.trade.master.core.model;

import lombok.*;

/**
 * Created by huseyina on 4/19/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexOrderResult {
    private PoloniexOpenOrder order;
    private PoloniexTradeResult tradeResult;
    private Boolean success;
    private String error;


    public PoloniexOrderResult(PoloniexOpenOrder order, PoloniexTradeResult tradeResult) {
        this.order = order;
        this.tradeResult = tradeResult;
        success = true;
    }

    public PoloniexOrderResult(PoloniexOpenOrder order, String error) {
        this.order = order;
        this.error = error;
        success = false;
    }

}
