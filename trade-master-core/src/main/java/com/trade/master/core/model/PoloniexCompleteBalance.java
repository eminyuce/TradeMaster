package com.trade.master.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexCompleteBalance implements Comparable {
    private Float available;
    private Float onOrders;
    private Float btcValue;
    @Override
    public int compareTo(Object o) {
        PoloniexCompleteBalance balance = (PoloniexCompleteBalance) o;
        return (int) (this.getBtcValue() - ((PoloniexCompleteBalance) o).getBtcValue());
    }
}
