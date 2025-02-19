package com.trade.master.core.service;

import com.trade.master.core.model.PoloniexTrade;

import java.util.List;
import java.util.Map;

/**
 * Created by habanoz on 22.04.2017.
 */
public interface TradeTrackerService {
    Map<String, List<PoloniexTrade>> returnTrades(boolean updateRecord);
}
