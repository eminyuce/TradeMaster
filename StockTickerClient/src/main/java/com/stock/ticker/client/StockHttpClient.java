package com.stock.ticker.client;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockHttpClient {

    private final HttpClient client;

    public StockHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

    public String postHttp(String url, List<Map.Entry<String, String>> params, List<Map.Entry<String, String>> headers) throws Exception {
        // Build form data
        String formData = params.stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        // Build HttpRequest for POST
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(formData));

        // Add headers if available
        if (headers != null) {
            for (Map.Entry<String, String> header : headers) {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }

        // Execute the request
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return handleResponse(response);
    }

    public String getHttp(String url, List<Map.Entry<String, String>> headers) throws Exception {
        // Build HttpRequest for GET
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET();

        // Add headers if available
        if (headers != null) {
            for (Map.Entry<String, String> header : headers) {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }

        // Execute the request
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return handleResponse(response);
    }

    private String handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode == 200) {
            return response.body(); // Success, return response body
        } else {
            // Handle error based on status code
            return "Error: " + statusCode + " - " + response.body();
        }
    }
}
