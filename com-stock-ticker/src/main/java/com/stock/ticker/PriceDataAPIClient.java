package com.stock.ticker;

/**
 *
 * @author David
 */
public interface PriceDataAPIClient
{
      String returnTicker();

      String getUSDBTCChartData(Long periodInSeconds, Long startEpochInSeconds);

      String getUSDETHChartData(Long periodInSeconds, Long startEpochInSeconds);

      String getChartData(String currencyPair, Long periodInSeconds, Long startEpochSeconds);

      String getChartData(String currencyPair, Long periodInSeconds, Long startEpochSeconds, Long endEpochSeconds);
}
