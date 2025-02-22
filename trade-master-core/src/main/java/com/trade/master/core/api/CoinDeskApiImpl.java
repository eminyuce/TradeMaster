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

    @Override
    public CoinDeskPrice getBtcPrice(String realCurrency) {
        return null;
    }

    @Override
    public CoinDeskPrice getBtcUsdPrice() {
        return getBtcPrice("usd");
    }
}
