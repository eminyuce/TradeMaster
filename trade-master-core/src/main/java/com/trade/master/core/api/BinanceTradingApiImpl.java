package com.trade.master.core.api;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stock.ticker.client.poloniex.PoloniexExchangeService;
import com.trade.master.core.entity.BotUser;
import com.trade.master.core.model.*;
import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BinanceTradingApiImpl implements BinanceTradingApi {

    @Autowired
    private SpotClient binanceClient;
    private BotUser botUser;

    public BinanceTradingApiImpl(BotUser botUser) {
        this.botUser = botUser;
    }


    public BotUser getBotUser() {
        return botUser;
    }

    @Override
    public Map runCommand(String commandName, List<org.apache.hc.core5.http.NameValuePair> params, TypeReference typeReference) {
        return Collections.emptyMap();
    }

    @Override
    public PoloniexOrderResult buy(Order order) {


        try {
            Map<String,Object> parameters = new LinkedHashMap<String,Object>();

            parameters.put("symbol","BTCUSDT");
            parameters.put("side", "SELL");
            parameters.put("type", "LIMIT");
            parameters.put("timeInForce", "GTC");
            parameters.put("quantity", 0.01);
            parameters.put("price", 9500);

            String result = binanceClient.createTrade().newOrder(parameters);
            log.info(result);
        } catch (BinanceConnectorException e) {
            log.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }

        return null;
    }


    @Override
    public PoloniexOrderResult sell(Order order) {
        return null;
    }

    @Override
    public Map runCommand(String commandName, TypeReference typeReference) {
        return runCommand(commandName, Collections.EMPTY_LIST, typeReference);
    }

    @Override
    public Map<String, List<PoloniexOpenOrder>> returnOpenOrders() {
        List<org.apache.hc.core5.http.NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("currencyPair", "all"));
        return runCommand("returnOpenOrders", params, new TypeReference<HashMap<String, List<PoloniexOpenOrder>>>() {
        });
    }

    @Override
    public Map<String, BigDecimal> returnBalances() {
        return runCommand("returnBalances", new TypeReference<HashMap<String, BigDecimal>>() {
        });
    }

    @Override
    public Map<String, PoloniexCompleteBalance> returnCompleteBalances() {
        Map<String, PoloniexCompleteBalance> completeBalanceMap = runCommand("returnCompleteBalances", new TypeReference<HashMap<String, PoloniexCompleteBalance>>() {
        });

        completeBalanceMap = completeBalanceMap.entrySet().stream().filter(map -> map.getValue().getBtcValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return completeBalanceMap;
    }

    @Override
    public BigDecimal returnBalance(String cur) {
        return returnBalances().get(cur);
    }

    @Override
    public Map<String, List<PoloniexTrade>> returnTradeHistory() {
        return returnTradeHistory(PoloniexExchangeService.LONG_LONG_AGO);
    }

    @Override
    public Map<String, List<PoloniexTrade>> returnTradeHistory(Long timeStampInSeconds) {
        return Collections.emptyMap();
    }

    @Override
    public boolean cancelOrder(String orderNumber) {
        return false;
    }
}
