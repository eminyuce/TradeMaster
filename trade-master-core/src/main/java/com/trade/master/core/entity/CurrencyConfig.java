package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"bot_user", "currencyPair"}))
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer currencyConfigId;

    private String currencyPair;
    private float usableBalancePercent;
    private float buyAtPrice = 0;
    private float buyOnPercent;
    private float sellAtPrice = 0;
    private float sellOnPercent;
    private Boolean buyable;
    private Boolean sellable;
    private float orderTimeoutInHour = 0;

    @ManyToOne
    @JoinColumn(name = "bot_user")
    private BotUser botUser;

    public CurrencyConfig(String currencyPair, float usableBalancePercent, float buyAtPrice, float buyOnPercent, float sellAtPrice, float sellOnPercent) {
        this.currencyPair = currencyPair;
        this.usableBalancePercent = usableBalancePercent;
        this.buyAtPrice = buyAtPrice;
        this.buyOnPercent = buyOnPercent;
        this.sellAtPrice = sellAtPrice;
        this.sellOnPercent = sellOnPercent;
    }
}
