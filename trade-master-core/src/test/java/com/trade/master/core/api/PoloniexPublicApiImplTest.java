package com.trade.master.core.api;

import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.entity.UserBot;
import com.trade.master.core.model.PoloniexTrade;
import com.trade.master.core.repository.BotRepository;
import com.trade.master.core.repository.CurrencyConfigRepository;
import com.trade.master.core.repository.UserBotRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by huseyina on 6/11/2017.
 */
@Disabled
public class PoloniexPublicApiImplTest {

    public static final String BTC_ETC="BTC_ETC";


    @Autowired
    private UserBotRepository userBotRepository;

    @Autowired
    private BotRepository botRepository;


    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;


    @Test
    public void returnTradeHistory() throws Exception {
        BinancePublicApiImpl poloniexPublicApi=new BinancePublicApiImpl();
        long end = System.currentTimeMillis() / 1000;
        long start = end - 60 * 10;

        List<PoloniexTrade> tradeHistory = poloniexPublicApi.returnTradeHistory(BTC_ETC, start, end);

    }

    @Test
    public void getCurrencyConfig()
    {
        List<BotUser> activeBotUsers = userBotRepository.findByBotQuery("PoloniexTradeTrackerBot").stream().map(UserBot::getUser).collect(Collectors.toList());
        for (BotUser user: activeBotUsers)
        {
            List<CurrencyConfig> currencyConfigs = currencyConfigRepository.findByBotUser(user)
                    .stream().filter(r -> r.getBuyable() || r.getSellable())
                    .sorted((f1, f2) -> Float.compare(f1.getUsableBalancePercent(), f2.getUsableBalancePercent()))
                    .collect(Collectors.toList());
                System.out.println(user.getUserEmail());
                System.out.println("----------------------");
            for (CurrencyConfig currency: currencyConfigs) {
                System.out.println(currency);
            }

        }


    }

}