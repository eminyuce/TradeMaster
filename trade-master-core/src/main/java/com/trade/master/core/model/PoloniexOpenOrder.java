package com.trade.master.core.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PoloniexOpenOrder {
    private String orderNumber;
    private String currencyPair;
    private String type;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal total;
    private Date date;

    public PoloniexOpenOrder(String currencyPair, String type, BigDecimal rate, BigDecimal amount) {
        this.currencyPair = currencyPair;
        this.type = type;
        this.rate = rate.setScale(12, BigDecimal.ROUND_DOWN);
        this.amount = amount.setScale(12, BigDecimal.ROUND_DOWN);
        this.total = rate.multiply(amount).setScale(12, BigDecimal.ROUND_DOWN);
    }

    public PoloniexOpenOrder(String currencyPair, String type, BigDecimal rate, BigDecimal amount, Date date) {
        this(currencyPair, type, rate, amount);
        this.date = date;
    }

    public PoloniexOpenOrder(Order order) {
        this(order.getCurrencyPair(), order.getType(), order.getRate(), order.getAmount(), order.getDate());
    }
}
