package com.trade.master.core.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/**
 * Created by huseyina on 5/19/2017.
 */
@Disabled
public class CoinDeskApiImplTest {
    @Test
    public void getBtcUsdPrice() throws Exception {
        CoinDeskApi coinDeskApi=new CoinDeskApiImpl();
        coinDeskApi.getBtcUsdPrice();
    }

}