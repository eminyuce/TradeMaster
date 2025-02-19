package com.trade.master.core.robot;

import com.trade.master.core.api.PoloniexTradingApi;
import com.trade.master.core.api.PoloniexTradingApiImpl;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyOrder;
import com.trade.master.core.entity.UserBot;
import com.trade.master.core.mail.HtmlHelper;
import com.trade.master.core.mail.MailService;
import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.CurrencyOrderRepository;
import com.trade.master.core.repository.TradeHistoryTrackRepository;
import com.trade.master.core.repository.UserBotRepository;
import com.trade.master.core.service.TradeTrackerService;
import com.trade.master.core.service.TradeTrackerServiceImpl;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by habanoz on 22.04.2017.
 */
@Component
public class PoloniexTradeTrackerBot {
    private static final Logger logger = LoggerFactory.getLogger(PoloniexTradeTrackerBot.class);

    @Autowired
    private UserBotRepository userBotRepository;

    @Autowired
    private TradeHistoryTrackRepository tradeHistoryTrackRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private HtmlHelper htmlHelper;

    @Autowired
    private CurrencyOrderRepository currencyOrderRepository;


    public PoloniexTradeTrackerBot() {

    }

    @PostConstruct
    public void init() {
    }


    @Scheduled(fixedDelay = 300000)
    public void runLogic() {

        logger.info("Bot started");

        List<BotUser> activeBotUsers = userBotRepository.findByBotQuery(getClass().getSimpleName()).stream().map(UserBot::getUser).collect(Collectors.toList());

        for (BotUser user : activeBotUsers) {
            startTradingForEachUser(user);
        }

        logger.info("Bot completed");
    }

    private void startTradingForEachUser(BotUser user) {

        PoloniexTradingApi poloniexTradingApi = new PoloniexTradingApiImpl(user);

        TradeTrackerService tradeTrackerService = new TradeTrackerServiceImpl(tradeHistoryTrackRepository, poloniexTradingApi, user);

        Map<String, List<PoloniexTrade>> recentTrades = tradeTrackerService.returnTrades(true);

        if (!recentTrades.isEmpty())
            logger.info("Recent trades found for user {}", user);


        if (!recentTrades.isEmpty()) {// if any of them is not empty send mail
            mailService.sendMail(user, "Recent Trades", htmlHelper.getSummaryHTML(Collections.EMPTY_LIST, recentTrades, poloniexTradingApi.returnCompleteBalances()), true);

            DeactivateUnfilledBuyOrders(user, recentTrades);

        }
    }

    private void DeactivateUnfilledBuyOrders(BotUser user, Map<String, List<PoloniexTrade>> recentTrades) {
        // BUY operation is fulfilled in the poloniex trading platfrom successfully, DeActive currency of BUY order
        try {
            for (Map.Entry<String, List<PoloniexTrade>> mapKey : recentTrades.entrySet()) {
                String key = mapKey.getKey();
                List<PoloniexTrade> poloniexTrades = mapKey.getValue().stream().filter(r -> r.getType().equalsIgnoreCase("BUY")).collect(Collectors.toList());
                if (poloniexTrades != null)
                    for (PoloniexTrade poloniexTrade : poloniexTrades) {
                        CurrencyOrder currencyOrder = currencyOrderRepository.findByUserIdAndOrderNumber(user.getId(), poloniexTrade.getOrderNumber());
                        if (currencyOrder != null) {
                            currencyOrder.setActive(false);
                            currencyOrderRepository.save(currencyOrder);
                        }
                    }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
