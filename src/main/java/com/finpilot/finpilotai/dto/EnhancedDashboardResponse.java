package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnhancedDashboardResponse {

    private Double totalIncome;

    private Double totalExpense;

    private Double savings;

    private Double goalProgress;

    private Double monthlyBudget;

    private Double remainingBudget;
}