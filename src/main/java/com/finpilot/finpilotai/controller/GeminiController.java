package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.GeminiResponse;
import com.finpilot.finpilotai.service.GeminiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(
            GeminiService geminiService) {

        this.geminiService = geminiService;
    }

    @GetMapping("/ai-advice")
    public GeminiResponse getAdvice() {

        return geminiService.getAdvice();
    }
}