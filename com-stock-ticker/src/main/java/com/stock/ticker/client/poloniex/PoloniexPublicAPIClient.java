package com.stock.ticker.client.poloniex;

import com.stock.ticker.PriceDataAPIClient;
import com.stock.ticker.client.StockHttpClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PoloniexPublicAPIClient implements PriceDataAPIClient
{

    private static final String PUBLIC_URL = "https://poloniex.com/public?";
    private final StockHttpClient client;

    public PoloniexPublicAPIClient()
    {
        this.client = new StockHttpClient();
    }

    public PoloniexPublicAPIClient(StockHttpClient client)
    {
        this.client = client;
    }

    @Override
    public String returnTicker()
    {
        try
        {
            String url = PUBLIC_URL + "command=returnTicker";
            return client.getHttp(url, null);
        }
        catch (Exception ex)
        {
            log.warn("Call to return ticker API resulted in exception - " + ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    public String getUSDBTCChartData(Long periodInSeconds, Long startEpochInSeconds)
    {
        String currencyPair = "USDT_BTC";
        return getChartData(currencyPair, periodInSeconds, startEpochInSeconds);
    }

    @Override
    public String getUSDETHChartData(Long periodInSeconds, Long startEpochInSeconds)
    {
        String currencyPair = "USDT_ETH";
        return getChartData(currencyPair, periodInSeconds, startEpochInSeconds);
    }

    @Override
    public String getChartData(String currencyPair, Long periodInSeconds, Long startEpochSeconds)
    {
        return getChartData(currencyPair, periodInSeconds, startEpochSeconds, 9999999999L);
    }

    @Override
    public String getChartData(String currencyPair, Long periodInSeconds, Long startEpochSeconds, Long endEpochSeconds)
    {
        return getChartData(currencyPair, startEpochSeconds.toString(), endEpochSeconds.toString(), periodInSeconds.toString());
    }

    private String getChartData(String currencyPair, String startEpochInSec, String endEpochInSec, String periodInSec)
    {
        try
        {
            String url = PUBLIC_URL + "command=returnChartData&currencyPair=" + currencyPair + "&start=" + startEpochInSec + "&end=" + endEpochInSec + "&period=" + periodInSec;
            return client.getHttp(url, null);
        }
        catch (Exception ex)
        {
            log.warn("Call to Chart Data API resulted in exception - " + ex.getMessage(), ex);
        }

        return null;
    }

}
