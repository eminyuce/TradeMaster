package com.trade.master.core.api;

import com.trade.master.core.model.CoinDeskPrice;

/**
 * Created by huseyina on 5/16/2017.
 */
public interface CoinDeskApi {
    CoinDeskPrice getBtcPrice(String realCurrency);

    CoinDeskPrice getBtcUsdPrice();
}
