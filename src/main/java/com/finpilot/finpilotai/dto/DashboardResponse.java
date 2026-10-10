package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponse {

    private Double totalIncome;

    private Double totalExpense;

    private Double savings;
}