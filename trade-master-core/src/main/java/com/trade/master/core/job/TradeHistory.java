package com.trade.master.core.job;

import com.trade.master.core.api.BinancePublicApi;
import com.trade.master.core.entity.AnalysisCurrencyConfig;
import com.trade.master.core.entity.TradeHistoryRecord;
import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.AnalysisCurrencyConfigRepository;
import com.trade.master.core.repository.TradeHistoryRecordRepository;
import com.trade.master.core.robot.PolBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huseyina on 6/10/2017.
 */
@Component
public class TradeHistory {

    @Autowired
    private BinancePublicApi publicApi;

    @Autowired
    private TradeHistoryRecordRepository tradeHistoryRecordRepository;

    @Autowired
    private AnalysisCurrencyConfigRepository analysisCurrencyConfigRepository;

    @PostConstruct
    public void init() {

    }

    @Scheduled(cron = "0 0/15 * * * ?")
    public void execute() {

        List<AnalysisCurrencyConfig> currencyConfigs = analysisCurrencyConfigRepository.findByEnabledTrue();
        for (AnalysisCurrencyConfig currencyConfig : currencyConfigs)
            execute4Curr(currencyConfig.getCurrencyPair());
    }

    private void execute4Curr(String currPair) {
        long end = System.currentTimeMillis() / 1000;
        long start = end - 60 * 15;

        List<PoloniexTrade> tradeHistory = publicApi.returnTradeHistory(currPair, start, end);
        BigDecimal buyVol = BigDecimal.valueOf(0);
        BigDecimal sellVol = BigDecimal.valueOf(0);
        BigDecimal buyQVol = BigDecimal.valueOf(0);
        BigDecimal sellQVol = BigDecimal.valueOf(0);
        BigDecimal low = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal high = BigDecimal.valueOf(0);
        BigDecimal open = tradeHistory.get(0).getRate();
        BigDecimal close = tradeHistory.get(tradeHistory.size() - 1).getRate();

        for (PoloniexTrade trade : tradeHistory) {

            if (trade.getType().equalsIgnoreCase(PolBot.BUY_ACTION))
                buyVol = buyVol.add(trade.getTotal());
            else
                sellVol = sellVol.add(trade.getTotal());

            if (trade.getType().equalsIgnoreCase(PolBot.BUY_ACTION))
                buyQVol = buyQVol.add(trade.getAmount());
            else
                sellQVol = sellQVol.add(trade.getAmount());

            if (trade.getRate().doubleValue() > high.doubleValue())
                high = trade.getRate();

            if (trade.getRate().doubleValue() < low.doubleValue())
                low = trade.getRate();

        }

        TradeHistoryRecord tradeHistoryRecord = new TradeHistoryRecord(currPair, start, buyVol.doubleValue(), sellVol.doubleValue(), buyQVol.doubleValue(), sellQVol.doubleValue(), open, close, high, low);
        tradeHistoryRecordRepository.save(tradeHistoryRecord);
    }
}
