package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.BudgetRequest;
import com.finpilot.finpilotai.dto.BudgetStatusResponse;
import com.finpilot.finpilotai.entity.Budget;
import com.finpilot.finpilotai.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(
            BudgetService budgetService) {

        this.budgetService = budgetService;
    }

    @PostMapping("/budget")
    public String addBudget(
            @Valid @RequestBody BudgetRequest request) {

        return budgetService.addBudget(request);
    }

    @GetMapping("/budget")
    public List<Budget> getMyBudget() {

        return budgetService.getMyBudget();
    }

    @GetMapping("/budget/status")
    public BudgetStatusResponse getBudgetStatus() {

        return budgetService.getBudgetStatus();
    }
}
