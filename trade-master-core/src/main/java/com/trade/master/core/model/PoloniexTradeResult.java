package com.trade.master.core.model;

import lombok.*;

import java.util.List;

/**
 * Created by huseyina on 4/7/2017.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexTradeResult {
    private String orderNumber;
    private List<PoloniexTrade> resultingTrades;

    public PoloniexTradeResult(List<PoloniexTrade> resultingTrades) {
        this.resultingTrades = resultingTrades;
    }

}
