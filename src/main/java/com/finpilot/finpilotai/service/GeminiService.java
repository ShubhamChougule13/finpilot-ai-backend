package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public GeminiResponse getAdvice() {

        GeminiResponse response =
                new GeminiResponse();

        response.setAdvice(
                "Gemini Key Length: " + geminiApiKey.length()
        );

        return response;
    }
}