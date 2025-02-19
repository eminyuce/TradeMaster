package com.stock.ticker;


import org.apache.hc.core5.http.NameValuePair;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author David
 */
public interface TradingAPIClient
{
      String returnBalances();

      String returnCompleteBalances();

      String returnFeeInfo();
    
      String returnOpenOrders();
    
      String returnTradeHistory(String currencyPair);
    
      String cancelOrder(String orderNumber);
    
      String moveOrder(String orderNumber, BigDecimal rate);

      String sell(String currencyPair, BigDecimal buyPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly);

      String buy(String currencyPair, BigDecimal buyPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly);

    String returnTradingAPICommandResults(String commandValue);

    String returnTradingAPICommandResults(String commandValue, List<NameValuePair> params);
}
