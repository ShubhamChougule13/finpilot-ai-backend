package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.genai.Client;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public GeminiResponse getAdvice() {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                        + geminiApiKey;
        System.out.println("Gemini URL: " + url);

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        String prompt = """
                You are a financial advisor.

                Analyze the following data and provide financial advice.

                Income: ₹60000
                Expense: ₹500
                Savings: ₹59500
                Goal Progress: 49.58%
                Remaining Budget: ₹19500
                Top Spending Category: Food

                Provide 3 short financial recommendations.
                """;

        Map<String, Object> part =
                Map.of(
                        "text",
                        prompt
                );

        Map<String, Object> content =
                Map.of(
                        "parts",
                        List.of(part)
                );

        Map<String, Object> requestBody =
                Map.of(
                        "contents",
                        List.of(content)
                );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        String responseBody =
                restTemplate.postForObject(
                        url,
                        request,
                        String.class
                );

        GeminiResponse response =
                new GeminiResponse();

        response.setAdvice(responseBody);

        return response;
    }
}