package com.stock.ticker.client.poloniex;

import com.stock.ticker.TradingAPIClient;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

/**
 * Poloniex Trading API client rewritten to use Java 17 HttpClient.
 *
 * @author David
 */
@Slf4j
public class PoloniexTradingAPIClient implements TradingAPIClient {

    private static final String TRADING_URL = "https://poloniex.com/tradingApi";
    private final String apiKey;
    private final String apiSecret;
    private final HttpClient httpClient;

    public PoloniexTradingAPIClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String returnBalances() {
        return returnTradingAPICommandResults("returnBalances");
    }

    @Override
    public String returnFeeInfo() {
        return returnTradingAPICommandResults("returnFeeInfo");
    }

    @Override
    public String returnCompleteBalances() {
        return returnTradingAPICommandResults("returnCompleteBalances");
    }

    @Override
    public String returnOpenOrders() {
        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("currencyPair", "all"));
        return returnTradingAPICommandResults("returnOpenOrders", additionalPostParams);
    }

    @Override
    public String returnTradeHistory(String currencyPair) {
        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("currencyPair", currencyPair == null ? "all" : currencyPair));
        additionalPostParams.add(new BasicNameValuePair("start", PoloniexExchangeService.LONG_LONG_AGO.toString()));
        return returnTradingAPICommandResults("returnTradeHistory", additionalPostParams);
    }

    @Override
    public String cancelOrder(String orderNumber) {
        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("orderNumber", orderNumber));
        return returnTradingAPICommandResults("cancelOrder", additionalPostParams);
    }

    @Override
    public String moveOrder(String orderNumber, BigDecimal rate) {
        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("orderNumber", orderNumber));
        additionalPostParams.add(new BasicNameValuePair("rate", rate.toPlainString()));
        additionalPostParams.add(new BasicNameValuePair("postOnly", "0"));
        additionalPostParams.add(new BasicNameValuePair("immediateOrCancel", "0"));
        return returnTradingAPICommandResults("moveOrder", additionalPostParams);
    }

    @Override
    public String sell(String currencyPair, BigDecimal sellPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly) {
        return trade("sell", currencyPair, sellPrice, amount, fillOrKill, immediateOrCancel, postOnly);
    }

    @Override
    public String buy(String currencyPair, BigDecimal buyPrice, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly) {
        return trade("buy", currencyPair, buyPrice, amount, fillOrKill, immediateOrCancel, postOnly);
    }

    private String trade(String tradeType, String currencyPair, BigDecimal rate, BigDecimal amount, boolean fillOrKill, boolean immediateOrCancel, boolean postOnly) {
        List<NameValuePair> additionalPostParams = new ArrayList<>();
        additionalPostParams.add(new BasicNameValuePair("currencyPair", currencyPair));
        additionalPostParams.add(new BasicNameValuePair("rate", rate.toPlainString()));
        additionalPostParams.add(new BasicNameValuePair("amount", amount.toPlainString()));
        additionalPostParams.add(new BasicNameValuePair("fillOrKill", fillOrKill ? "1" : "0"));
        additionalPostParams.add(new BasicNameValuePair("immediateOrCancel", immediateOrCancel ? "1" : "0"));
        additionalPostParams.add(new BasicNameValuePair("postOnly", postOnly ? "1" : "0"));
        return returnTradingAPICommandResults(tradeType, additionalPostParams);
    }

    @Override
    public String returnTradingAPICommandResults(String commandValue, List<NameValuePair> additionalPostParams) {
        try {
            // Build required post parameters
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("command", commandValue));
            postParams.add(new BasicNameValuePair("nonce", String.valueOf(System.currentTimeMillis())));
            if (additionalPostParams != null && !additionalPostParams.isEmpty()) {
                postParams.addAll(additionalPostParams);
            }

            // Create URL-encoded body string
            String body = postParams.stream()
                    .map(param -> param.getName() + "=" + param.getValue())
                    .collect(Collectors.joining("&"));

            // Generate HMAC-SHA512 signature
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA512"));
            String signature = new String(Hex.encodeHex(mac.doFinal(body.getBytes())));

            // Build the HTTP POST request using Java 17 HttpClient
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(TRADING_URL))
                    .header("Key", apiKey)
                    .header("Sign", signature)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            // Send the request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.warn("Call to Poloniex Trading API resulted in exception - " + ex.getMessage(), ex);
        } catch (Exception ex) {
            log.warn("Unexpected exception - " + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String returnTradingAPICommandResults(String commandValue) {
        return returnTradingAPICommandResults(commandValue, new ArrayList<>());
    }
}
