package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetStatusResponse {

    private Double monthlyBudget;

    private Double spentAmount;

    private Double remainingBudget;
}
