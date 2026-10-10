package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseCategoryReportResponse {

    private String category;

    private Double totalAmount;
}