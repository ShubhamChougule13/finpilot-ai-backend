package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.EnhancedDashboardResponse;
import com.finpilot.finpilotai.dto.GeminiResponse;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.finpilot.finpilotai.dto.TopCategoryResponse;

@Service
public class GeminiSdkService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final DashboardService dashboardService;
    private final ReportService reportService;

    public GeminiSdkService(
            DashboardService dashboardService,
            ReportService reportService) {

        this.dashboardService = dashboardService;
        this.reportService = reportService;
    }

    public GeminiResponse testGemini() {

        EnhancedDashboardResponse dashboard =
                dashboardService.getEnhancedDashboard();
        TopCategoryResponse topCategory =
                reportService.getTopCategory();

        String prompt = """
                You are an expert financial advisor.

                Analyze the following financial data and provide
                3 short personalized recommendations.

                Income: %s
                Expense: %s
                Savings: %s
                Goal Progress: %s%%
                Monthly Budget: %s
                Remaining Budget: %s
                Top Spending Category: %s
                Top Spending Amount: %s

                Keep the advice concise and practical.
                """
                .formatted(
                        dashboard.getTotalIncome(),
                        dashboard.getTotalExpense(),
                        dashboard.getSavings(),
                        dashboard.getGoalProgress(),
                        dashboard.getMonthlyBudget(),
                        dashboard.getRemainingBudget(),
                        topCategory.getCategory(),
                        topCategory.getAmount()
                );

        Client client =
                Client.builder()
                        .apiKey(geminiApiKey)
                        .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3-flash-preview",
                        prompt,
                        null
                );

        GeminiResponse geminiResponse =
                new GeminiResponse();

        geminiResponse.setAdvice(
                response.text()
        );

        return geminiResponse;
    }
}