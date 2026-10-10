package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.DashboardResponse;
import com.finpilot.finpilotai.dto.EnhancedDashboardResponse;
import com.finpilot.finpilotai.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {

        return dashboardService.getDashboard();
    }

    @GetMapping("/dashboard/enhanced")
    public EnhancedDashboardResponse getEnhancedDashboard() {

        return dashboardService.getEnhancedDashboard();
    }
}