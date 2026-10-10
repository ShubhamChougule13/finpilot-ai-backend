package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.AIInsightResponse;
import com.finpilot.finpilotai.service.AIInsightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIInsightController {

    private final AIInsightService aiInsightService;

    public AIInsightController(
            AIInsightService aiInsightService) {

        this.aiInsightService = aiInsightService;
    }

    @GetMapping("/ai-insights")
    public AIInsightResponse getInsights() {

        return aiInsightService.getInsights();
    }
}