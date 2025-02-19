package com.trade.master.core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.ticker.client.StockHttpClient;
import com.trade.master.core.model.CoinDeskCurrentPrice;
import com.trade.master.core.model.CoinDeskPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by huseyina on 5/16/2017.
 */
@Component
@Slf4j
public class CoinDeskApiImpl implements CoinDeskApi {

    private static final String REAL_CURRENCY_STR = "%REAL_CURRENCY%";
    private static final String PUBLIC_URL = "http://api.coindesk.com/v1/bpi/currentprice/" + REAL_CURRENCY_STR + ".json";

    @Autowired
    private StockHttpClient client;

    @Override
    public CoinDeskPrice getBtcPrice(String realCurrency) {
        try {
            realCurrency = realCurrency.toUpperCase();
            String url = PUBLIC_URL.replace(REAL_CURRENCY_STR, realCurrency);
            String tickerJsonStr = client.getHttp(url, null);
            CoinDeskCurrentPrice coinDeskCurrentPrice = new ObjectMapper().readValue(tickerJsonStr, new TypeReference<CoinDeskCurrentPrice>() {
            });

            return coinDeskCurrentPrice.getBpi().get(realCurrency);

        } catch (Exception ex) {
            log.warn("Call to coindesk API resulted in exception - " + ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    public CoinDeskPrice getBtcUsdPrice() {
        return getBtcPrice("usd");
    }
}
