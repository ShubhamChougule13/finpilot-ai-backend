package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.ExpenseCategoryReportResponse;
import com.finpilot.finpilotai.dto.TopCategoryResponse;
import com.finpilot.finpilotai.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping("/reports/categories")
    public List<ExpenseCategoryReportResponse> getCategoryReport() {

        return reportService.getCategoryReport();
    }

    @GetMapping("/reports/top-category")
    public TopCategoryResponse getTopCategory() {

        return reportService.getTopCategory();
    }
}