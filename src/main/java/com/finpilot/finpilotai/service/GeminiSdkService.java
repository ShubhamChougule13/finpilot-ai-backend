package com.finpilot.finpilotai.service;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiSdkService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String testGemini() {

        Client client =
                Client.builder()
                        .apiKey(geminiApiKey)
                        .build();

        System.out.println(client.models);

        return "Gemini SDK Working";
    }
}