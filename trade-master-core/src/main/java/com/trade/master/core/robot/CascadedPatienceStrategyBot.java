package com.trade.master.core.robot;

import com.trade.master.core.api.PoloniexPublicApi;
import com.trade.master.core.api.PoloniexPublicApiImpl;
import com.trade.master.core.api.PoloniexTradingApi;
import com.trade.master.core.api.PoloniexTradingApiImpl;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.mail.HtmlHelper;
import com.trade.master.core.mail.MailService;
import com.trade.master.core.model.*;
import com.trade.master.core.repository.CurrencyConfigRepository;
import com.trade.master.core.repository.TradeHistoryTrackRepository;
import com.trade.master.core.service.TradeTrackerServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Trading bot with Buy when cheap sell when high logic
 * <p>
 * Created by habanoz on 05.04.2017.
 */
@Component
@Slf4j
public class CascadedPatienceStrategyBot extends PoloniexPatienceStrategyBot {

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;

    @Autowired
    private HtmlHelper htmlHelper;

    @Autowired
    private MailService mailService;

    @Autowired
    private TradeHistoryTrackRepository tradeHistoryTrackRepository;

    private static final String BASE_CURR = "BTC";
    private static final String CURR_PAIR_SEPARATOR = "_";


    public CascadedPatienceStrategyBot() {
    }

    @PostConstruct
    public void init() {
    }

    @Scheduled(fixedDelay = 300000)
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void startTradingForEachUser(BotUser user, Map<String, PoloniexTicker> tickerMap) {
        log.info("Started for user {}", user);

        List<CurrencyConfig> currencyConfigs = getCurrencyConfigs(user);

        if (currencyConfigs.isEmpty()) {
            log.info("No currency config for user {}, returning ...", user);
            return;
        }

        //create tradingApi instance for current user
        PoloniexTradingApi tradingApi = new PoloniexTradingApiImpl(user);

        PoloniexPublicApi publicApi = new PoloniexPublicApiImpl();

        Map<String, List<PoloniexOpenOrder>> openOrderMap = tradingApi.returnOpenOrders();
        Map<String, BigDecimal> balanceMap = tradingApi.returnBalances();
        Map<String, PoloniexCompleteBalance> completeBalanceMap = tradingApi.returnCompleteBalances();

        final int emaTimeFrame = 12;
        final long periodInSec = 300L;
        long startTime = System.currentTimeMillis() - (emaTimeFrame * 2) * periodInSec * 1000;


        Map<String, List<PoloniexTrade>> historyMap = tradingApi.returnTradeHistory();

        Map<String, List<PoloniexTrade>> recentHistoryMap = new TradeTrackerServiceImpl(tradeHistoryTrackRepository, tradingApi, user).returnTrades(true);

        BigDecimal btcBalance = balanceMap.get(BASE_CURR);

        List<PoloniexOrderResult> orderResults = new ArrayList<>();

        for (CurrencyConfig currencyConfig : currencyConfigs) {

            String currPair = currencyConfig.getCurrencyPair();

            List<PoloniexChart> chartData = publicApi.returnChart(currPair, periodInSec, startTime, Long.MAX_VALUE);

            PolStrategy patienceStrategy = new CascadedPatienceStrategy(currencyConfig, chartData, emaTimeFrame);

            String currName = currPair.split(CURR_PAIR_SEPARATOR)[1];

            BigDecimal currBalance = balanceMap.get(currName);

            // this may indicate invalid currency name
            if (tickerMap == null)
                continue;

            PoloniexTicker ticker = tickerMap.get(currPair);

            // this may indicate invalid currency name
            if (ticker == null)
                continue;

            //current lowest market price
            BigDecimal lowestBuyPrice = ticker.getLowestAsk();
            BigDecimal highestSellPrice = ticker.getHighestBid();

            Date now = new Date();

            List<Order> orders = patienceStrategy.execute(
                    new PoloniexChart(new BigDecimal(now.getTime()), lowestBuyPrice, lowestBuyPrice, lowestBuyPrice, lowestBuyPrice, ticker.getBaseVolume()),
                    btcBalance, currBalance, openOrderMap.get(currPair), historyMap.get(currPair), recentHistoryMap.get(currPair)
            );

            btcBalance = createOrders(user, tradingApi, btcBalance, orderResults, orders);

            cancelOrders(tradingApi, openOrderMap.get(currPair), patienceStrategy, now);

        }

        sendNotificationMail(user, completeBalanceMap, recentHistoryMap, orderResults);

        log.info("Completed for user {}", user);
    }

}
