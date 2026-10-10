package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIInsightResponse {

    private String message;

    private Double savings;

    private Double goalProgress;

    private Double remainingBudget;
}