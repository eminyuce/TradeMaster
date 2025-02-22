package com.stock.ticker.client;

import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockHttpClient {

    private final HttpClient client;

    public StockHttpClient() {
        this.client = createSecureHttpClient();
    }

    private HttpClient createSecureHttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3"); // Ensure TLS 1.2 or 1.3
            sslContext.init(null, new TrustManager[]{new DefaultTrustManager()}, new java.security.SecureRandom());

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to initialize SSL context", e);
        }
    }

    public String postHttp(String url, List<Map.Entry<String, String>> params, List<Map.Entry<String, String>> headers) throws Exception {
        String formData = params.stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(formData));

        if (headers != null) {
            headers.forEach(header -> requestBuilder.header(header.getKey(), header.getValue()));
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return handleResponse(response);
    }

    public String getHttp(String url, List<Map.Entry<String, String>> headers) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET();

        if (headers != null) {
            headers.forEach(header -> requestBuilder.header(header.getKey(), header.getValue()));
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return handleResponse(response);
    }

    private String handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode == 200) {
            return response.body();
        } else {
            return "Error: " + statusCode + " - " + response.body();
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
}
