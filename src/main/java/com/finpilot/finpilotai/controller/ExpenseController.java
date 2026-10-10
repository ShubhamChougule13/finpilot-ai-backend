package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.ExpenseRequest;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expenses")
    public String addExpense(
            @Valid @RequestBody ExpenseRequest request) {

        return expenseService.addExpense(request);
    }

    @GetMapping("/expenses")
    public List<Expense> getMyExpenses() {

        return expenseService.getMyExpenses();
    }
    @PutMapping("/expenses/{id}")
    public String updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {

        return expenseService.updateExpense(id, request);
    }
    @DeleteMapping("/expenses/{id}")
    public String deleteExpense(
            @PathVariable Long id) {

        return expenseService.deleteExpense(id);
    }
}