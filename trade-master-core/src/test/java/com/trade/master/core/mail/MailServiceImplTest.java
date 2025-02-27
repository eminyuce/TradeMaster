package com.trade.master.core.mail;

import com.trade.master.core.api.PoloniexPublicApi;
import com.trade.master.core.api.PoloniexTradingApi;
import com.trade.master.core.api.PoloniexTradingApiImpl;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.entity.CurrencyConfig;
import com.trade.master.core.model.PoloniexCompleteBalance;
import com.trade.master.core.model.PoloniexOpenOrder;
import com.trade.master.core.model.PoloniexTicker;
import com.trade.master.core.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huseyina on 4/9/2017.
 */
@Disabled
public class MailServiceImplTest {


    private static final double minAmount = 0.0001;
    private static final long BUY_SELL_SLEEP = 100;
    private static final String BASE_CURR = "BTC";
    private static final String CURR_PAIR_SEPARATOR = "_";


    @Autowired
    MailService mailService;

    @Autowired
    private PoloniexPublicApi publicApi;

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;

    @Autowired
    private BotUserRepository botUserRepository;

    @Autowired
    private UserBotRepository userBotRepository;

    @Autowired
    private TradeHistoryTrackRepository tradeHistoryTrackRepository;


    @Autowired
    private CurrencyOrderRepository currencyOrderRepository;
    @Test
    public void sendMail() throws Exception {
        mailService.sendMail("huseyinabanox@gmail.com", "merab", "naber", true);
    }

    @Test
    public void sortingExample() throws Exception {
        int userId = 1;
        //User specific currency config list
        BotUser user = botUserRepository.getReferenceById(userId);
        PoloniexTradingApi poloniexTradingApi = new PoloniexTradingApiImpl(user);

        Map<String, PoloniexCompleteBalance> balancesMap=poloniexTradingApi.returnCompleteBalances();
        balancesMap =         balancesMap.entrySet()
                .stream()
                .filter(map -> map.getValue().getBtcValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for(Map.Entry<String, PoloniexCompleteBalance> mapKey : balancesMap.entrySet()) {
            String key = mapKey.getKey();
            PoloniexCompleteBalance m = mapKey.getValue();
            System.out.println(key+"="+m.getBtcValue());
        }

    }
    @Test
    public void calculateTradingBTCForEachCurrency() throws Exception {
        int userId = 1;
        //User specific currency config list
        BotUser user = botUserRepository.getReferenceById(userId);

        //Just get Buyable currencies from db.
        List<CurrencyConfig> currencyConfigs = currencyConfigRepository.findByBotUser((user))
                .stream().filter(r -> r.getBuyable() || r.getSellable())
                .sorted((f1, f2) -> Float.compare(f1.getUsableBalancePercent(), f2.getUsableBalancePercent()))
                .collect(Collectors.toList());

        //create tradingApi instance for current user
        PoloniexTradingApi tradingApi = new PoloniexTradingApiImpl(user);

        Map<String, BigDecimal> balanceMap = tradingApi.returnBalances();
        BigDecimal btcBalance = balanceMap.get(BASE_CURR);  // Total available balance for that cycle.
        Map<String, List<PoloniexOpenOrder>> openOrderMap = tradingApi.returnOpenOrders();

        System.out.println("Total Available btcBalance: "+btcBalance);

        // Distribute the available btc based on the Usable Balance Percent of each currency.
        HashMap<String, BigDecimal> tradingBTCMap  = getBTCTradingMap(currencyConfigs, btcBalance, openOrderMap);

        BigDecimal totalBalance = new BigDecimal(0);
        int index=1;
        for(Map.Entry<String, BigDecimal> mapKey : tradingBTCMap.entrySet()) {
            String key = mapKey.getKey();
            System.out.println(index+")"+key+" "+mapKey.getValue().doubleValue());
            index++;
            totalBalance = totalBalance.add(mapKey.getValue());
        }
        System.out.println("Total Balance: "+totalBalance+"  BtcBalance: "+btcBalance);
    }

    private HashMap<String, BigDecimal>  getBTCTradingMap(List<CurrencyConfig> currencyConfigs, BigDecimal btcBalance, Map<String, List<PoloniexOpenOrder>> openOrderMap) {
        HashMap<String, BigDecimal> tradingBTCMap = new HashMap<>();

        btcBalance = CalculationForUsableBTC(tradingBTCMap, currencyConfigs, btcBalance, openOrderMap, true );
        while (btcBalance.doubleValue() > minAmount) {  // Loop through until the available BTC is over
            double initialBtcValue = btcBalance.doubleValue();
            btcBalance = CalculationForUsableBTC(tradingBTCMap, currencyConfigs, btcBalance, openOrderMap, false);
            if(initialBtcValue == btcBalance.doubleValue()){
                break;
            }
         }

        if(currencyConfigs.size()>0 && tradingBTCMap.keySet().size() > 0){
            Map.Entry<String, BigDecimal> mapKey = tradingBTCMap.entrySet().iterator().next();
            BigDecimal  buyBudget = new BigDecimal( btcBalance.doubleValue() + tradingBTCMap.get(mapKey.getKey()).doubleValue());
            tradingBTCMap.put(mapKey.getKey(),buyBudget);
            btcBalance = btcBalance.subtract(buyBudget);
        }
        return tradingBTCMap;
    }


    private BigDecimal CalculationForUsableBTC(HashMap<String, BigDecimal> tradingBTCMap,
                                               List<CurrencyConfig> currencyConfigs,
                                               BigDecimal btcBalance, Map<String,
            List<PoloniexOpenOrder>> openOrderMap,
                                               boolean isMultiplierForEachCurrencyEnabled ) {

        if(btcBalance.doubleValue() <= 0){
            return btcBalance;
        }

        for (CurrencyConfig currencyConfig : currencyConfigs) {


            String currPair = currencyConfig.getCurrencyPair();
            String currName = currPair.split(CURR_PAIR_SEPARATOR)[1];
            List<PoloniexOpenOrder> openOrderListForCurr = openOrderMap.get(currPair);
            // Just calculate BTC value for the currencies who does not have any buy order
            if (!openOrderListForCurr.stream().anyMatch(r -> r.getType().equalsIgnoreCase("BUY"))) {

                //
                BigDecimal buyBudget = new BigDecimal(minAmount);
                if(isMultiplierForEachCurrencyEnabled){
                    buyBudget = new BigDecimal(minAmount * currencyConfig.getUsableBalancePercent());
                }
                if (tradingBTCMap.containsKey(currName)) {
                    buyBudget = new BigDecimal( buyBudget.doubleValue() + tradingBTCMap.get(currName).doubleValue());
                    tradingBTCMap.put(currName, buyBudget);
                } else {
                    tradingBTCMap.put(currName, buyBudget);
                }
                if(isMultiplierForEachCurrencyEnabled){
                    btcBalance = btcBalance.subtract(buyBudget);
                }else{
                    btcBalance = btcBalance.subtract(new BigDecimal(minAmount));
                }
            }else{

            }
            if(btcBalance.doubleValue() <= minAmount){
                return btcBalance;
            }
        }


        return btcBalance;
    }

    @Test
    public void saveAllCurrencies() throws Exception {

        Map<String, PoloniexTicker> tickers = publicApi.returnTicker();
        List<BotUser> botUsers = this.botUserRepository.findAll();
//        for (BotUser botUser:botUsers
//             ) {
//            for (Map.Entry<String, PoloniexTicker> entry : tickers.entrySet()) {
//                List<CurrencyConfig>  c = currencyConfigRepository.findByUserId(botUser.getUserId()).stream().filter(r->r.getCurrencyPair().equals((entry.getKey()))).collect(Collectors.toList());
//
//
//if(c.size() == 0){
//    System.out.println(entry.getKey());
//    CurrencyConfig currencyConfig = new CurrencyConfig();
//    currencyConfig.setBuyable(false);
//    currencyConfig.setBuyOnPercent(10);
//    currencyConfig.setBuyAtPrice(0);
//    currencyConfig.setSellable(false);
//    currencyConfig.setSellAtPrice(0);
//    currencyConfig.setSellOnPercent(10);
//    currencyConfig.setCurrencyPair(entry.getKey());
//    currencyConfig.setUserId(botUser.getUserId());
//    currencyConfigRepository.save(currencyConfig);
//}
//
//            }
//        }


    }
    @Test
    public void collectiveOrderForCurrency() throws Exception {
    //    collectiveOrders();
    }


}