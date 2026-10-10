package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.IncomeRequest;
import com.finpilot.finpilotai.entity.Income;
import com.finpilot.finpilotai.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(
            IncomeService incomeService) {

        this.incomeService = incomeService;
    }

    @PostMapping("/income")
    public String addIncome(
            @Valid @RequestBody IncomeRequest request) {

        return incomeService.addIncome(request);
    }

    @GetMapping("/income")
    public List<Income> getMyIncome() {

        return incomeService.getMyIncome();
    }

    @PutMapping("/income/{id}")
    public String updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequest request) {

        return incomeService.updateIncome(
                id,
                request);
    }

    @DeleteMapping("/income/{id}")
    public String deleteIncome(
            @PathVariable Long id) {

        return incomeService.deleteIncome(id);
    }
}