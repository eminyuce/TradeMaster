package com.trade.master.core.service;

import com.trade.master.core.api.PoloniexTradingApi;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.TradeHistoryTrack;
import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.TradeHistoryTrackRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by habanoz on 22.04.2017.
 */
public class TradeTrackerServiceImpl implements TradeTrackerService {

    private TradeHistoryTrackRepository tradeHistoryTrackRepository;
    private PoloniexTradingApi tradingApi;
    private BotUser user;

    public TradeTrackerServiceImpl(TradeHistoryTrackRepository tradeHistoryTrackRepository, PoloniexTradingApi poloniexTradingApi, BotUser user) {
        this.tradeHistoryTrackRepository = tradeHistoryTrackRepository;
        this.tradingApi = poloniexTradingApi;
        this.user = user;
    }

    @Override
    public Map<String, List<PoloniexTrade>> returnTrades(boolean updateRecord) {
        TradeHistoryTrack tradeHistoryTrack = tradeHistoryTrackRepository.getReferenceById(user.getId());


        if (tradeHistoryTrack == null) {
            Long startInSec = System.currentTimeMillis() / 1000 - 24 * 60 * 60;
            tradeHistoryTrack = new TradeHistoryTrack(user.getId(), startInSec);

            updateRecord = true;
        }

        Map<String, List<PoloniexTrade>> recentHistoryMap = tradingApi.returnTradeHistory(tradeHistoryTrack.getLastTimeStampInSec());

        // update record so that same trade records not used again
        if (updateRecord) {
            tradeHistoryTrack.setLastTimeStampInSec(System.currentTimeMillis() / 1000);//to sec
            tradeHistoryTrackRepository.save(tradeHistoryTrack);
        }

        return recentHistoryMap;
    }
}
