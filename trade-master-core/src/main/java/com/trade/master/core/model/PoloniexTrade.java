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
public class PoloniexTrade {
    private Long globalTradeID;
    private String tradeID;
    private Date date;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal total;
    private BigDecimal fee;
    private String orderNumber;
    private String type;
    private String category;

    public PoloniexTrade(BigDecimal rate, BigDecimal amount, String type) {
        this.rate = rate.setScale(12, BigDecimal.ROUND_DOWN);
        this.amount = amount.setScale(12, BigDecimal.ROUND_DOWN);
        this.type = type;
        this.total = rate.multiply(amount).setScale(12, BigDecimal.ROUND_DOWN);
    }

    public PoloniexTrade(Date date, BigDecimal rate, BigDecimal amount, BigDecimal fee, String type) {
        this(rate, amount, type);
        this.date = date;
        this.fee = fee;
    }

}
