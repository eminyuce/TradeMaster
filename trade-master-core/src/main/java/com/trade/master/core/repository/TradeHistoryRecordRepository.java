package com.trade.master.core.repository;

import com.trade.master.core.entity.TradeHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRecordRepository
        extends JpaRepository<TradeHistoryRecord, Integer> {

    List<TradeHistoryRecord> findByCurrencyPair(String currencyPair);

}
