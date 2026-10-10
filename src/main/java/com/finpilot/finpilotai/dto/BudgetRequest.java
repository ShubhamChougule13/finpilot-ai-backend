package com.finpilot.finpilotai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRequest {

    @NotNull(message = "Monthly budget is required")
    private Double monthlyBudget;

    private String description;
}