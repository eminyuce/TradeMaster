package com.stock.ticker.data.map.poloniex;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.stock.ticker.data.model.poloniex.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author David
 */
public class PoloniexDataMapper
{
    private final Gson gson;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PoloniexDataMapper()
    {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>()
        {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
            {
                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DTF);
            }
        }).create();
    }

    public List<PoloniexChartData> mapChartData(String chartDataResult)
    {
        PoloniexChartData[] chartDataResults = gson.fromJson(chartDataResult, PoloniexChartData[].class);
        return Arrays.asList(chartDataResults);
    }

    public PoloniexFeeInfo mapFeeInfo(String feeInfoResult)
    {
        PoloniexFeeInfo feeInfo = gson.fromJson(feeInfoResult, new TypeToken<PoloniexFeeInfo>()
        {
        }.getType());

        return feeInfo;
    }

    public PoloniexTicker mapTickerForCurrency(String currencyType, String tickerData)
    {
        Map<String, PoloniexTicker> tickerResults = gson.fromJson(tickerData, new TypeToken<Map<String, PoloniexTicker>>()
        {
        }.getType());
        return tickerResults.get(currencyType);
    }

    public PoloniexCompleteBalance mapCompleteBalanceResultForCurrency(String currencyType, String completeBalanceResults)
    {
        Map<String, PoloniexCompleteBalance> balanceResults = gson.fromJson(completeBalanceResults, new TypeToken<Map<String, PoloniexCompleteBalance>>()
        {
        }.getType());
        return balanceResults.get(currencyType);
    }

    public List<PoloniexOpenOrder> mapOpenOrders(String currencyPair, String openOrdersResults)
    {
        Map<String, List<PoloniexOpenOrder>> openOrders = gson.fromJson(openOrdersResults, new TypeToken<Map<String, List<PoloniexOpenOrder>>>()
        {
        }.getType());
        return openOrders.get(currencyPair);
    }

    public List<PoloniexTradeHistory> mapTradeHistory(String tradeHistoryResults)
    {
        List<PoloniexTradeHistory> tradeHistory = gson.fromJson(tradeHistoryResults, new TypeToken<List<PoloniexTradeHistory>>()
        {
        }.getType());
        return tradeHistory;
    }

    public boolean mapCancelOrder(String cancelOrderResult)
    {
        int success = gson.fromJson(cancelOrderResult, JsonObject.class).get("success").getAsInt();
        return success == 1;
    }

    public PoloniexOrderResult mapTradeOrder(String orderResult)
    {
        PoloniexOrderResult tradeOrderResult = gson.fromJson(orderResult, new TypeToken<PoloniexOrderResult>()
        {
        }.getType());
        return tradeOrderResult;
    }

}
