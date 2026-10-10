package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.service.GeminiSdkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiSdkController {

    private final GeminiSdkService geminiSdkService;

    public GeminiSdkController(
            GeminiSdkService geminiSdkService) {

        this.geminiSdkService = geminiSdkService;
    }

    @GetMapping("/gemini-sdk-test")
    public String testGemini() {

        return geminiSdkService.testGemini();
    }
}