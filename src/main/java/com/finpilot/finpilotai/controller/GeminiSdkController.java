package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.service.GeminiSdkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.finpilot.finpilotai.dto.GeminiResponse;

@RestController
public class GeminiSdkController {

    private final GeminiSdkService geminiSdkService;

    public GeminiSdkController(
            GeminiSdkService geminiSdkService) {

        this.geminiSdkService = geminiSdkService;
    }

    @GetMapping("/ai-advice")
    public GeminiResponse getAiAdvice() {

        return geminiSdkService.testGemini();
    }
}