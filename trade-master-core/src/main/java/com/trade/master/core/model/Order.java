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
public class Order {
    public static final int DEFAULT = 0;
    public static final int FILL_OR_KILL = 1;
    public static final int IMMEDIATE_OR_CANCEL = 2;
    public static final int POST_ONLY = 3;

    private String orderNumber;
    private String currencyPair;
    private String type;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal total;
    private Date date;
    private int mode = 0;

    public Order(String currencyPair, String type, BigDecimal rate, BigDecimal amount) {
        this.currencyPair = currencyPair;
        this.type = type;
        this.rate = rate.setScale(12, BigDecimal.ROUND_DOWN);
        this.amount = amount.setScale(12, BigDecimal.ROUND_DOWN);
        this.total = rate.multiply(amount).setScale(12, BigDecimal.ROUND_DOWN);
    }

    public Order(String currencyPair, String type, BigDecimal rate, BigDecimal amount, Date date) {
        this(currencyPair, type, rate, amount);
        this.date = date;
    }

    public Order(String currencyPair, String type, BigDecimal rate, BigDecimal amount, Date date, int mode) {
        this(currencyPair, type, rate, amount, date);
        this.mode = mode;
    }
}
