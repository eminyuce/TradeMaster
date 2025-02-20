package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by huseyina on 6/10/2017.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeHistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String currencyPair;
    private Long start;
    private Double buyVol;
    private Double sellVol;
    private Double buyQVol;
    private Double sellQVol;

    @Column(precision = 12, scale = 9)
    private BigDecimal open;

    @Column(precision = 12, scale = 9)
    private BigDecimal close;

    @Column(precision = 12, scale = 9)
    private BigDecimal high;

    @Column(precision = 12, scale = 9)
    private BigDecimal low;

    public TradeHistoryRecord(TradeHistoryRecord tradeHistoryRecord) {
        this.currencyPair = tradeHistoryRecord.getCurrencyPair();
        this.buyVol = tradeHistoryRecord.getBuyVol();
        this.buyQVol = tradeHistoryRecord.getBuyQVol();
        this.sellVol = tradeHistoryRecord.getSellVol();
        this.sellQVol = tradeHistoryRecord.getSellQVol();
        this.start = tradeHistoryRecord.getStart();
        this.open = tradeHistoryRecord.getOpen();
        this.close = tradeHistoryRecord.getClose();
        this.high = tradeHistoryRecord.getHigh();
        this.low = tradeHistoryRecord.getLow();
    }
    public TradeHistoryRecord(String currencyPair, Long start, Double buyVol, Double sellVol, Double buyQVol, Double sellQVol) {
        this.currencyPair = currencyPair;
        this.start = start;
        this.buyVol = buyVol;
        this.sellVol = sellVol;
        this.buyQVol = buyQVol;
        this.sellQVol = sellQVol;
    }

    public TradeHistoryRecord(String currencyPair, Long start, Double buyVol, Double sellVol, Double buyQVol, Double sellQVol, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low) {
        this.currencyPair = currencyPair;
        this.start = start;
        this.buyVol = buyVol;
        this.sellVol = sellVol;
        this.buyQVol = buyQVol;
        this.sellQVol = sellQVol;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }
}
