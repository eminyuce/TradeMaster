package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by habanoz on 05.04.2017.
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BotTradeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeConfigId;

    private String currencyPair;
    private float usableBalance;
    private BigDecimal buyAtPrice;
    private BigDecimal buyAtPriceLow;
    private BigDecimal buyAtPriceHigh;
    private int buySplitHalfCount;
    private BigDecimal sellAtPrice;
    private BigDecimal sellAtPriceLow;
    private BigDecimal sellAtPriceHigh;
    private int sellSplitHalfCount;
    private BigDecimal sellModePrice;
    private BigDecimal stopLossPrice;
    private Integer sellMode;

    @ManyToOne
    @JoinColumn(name = "bot_user")
    private BotUser botUser;

    private float orderTimeoutInHour = 0;
    private int completed;
    private int sellOrderGiven;
    private int buyOrderGiven;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date created = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updated = new Date();
}
