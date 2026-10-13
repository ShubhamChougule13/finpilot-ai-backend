
package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.BudgetRequest;
import com.finpilot.finpilotai.dto.BudgetStatusResponse;
import com.finpilot.finpilotai.entity.Budget;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.BudgetRepository;
import com.finpilot.finpilotai.repository.ExpenseRepository;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            ExpenseRepository expenseRepository) {

        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public String addBudget(BudgetRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Budget budget = new Budget();

        budget.setMonthlyBudget(request.getMonthlyBudget());
        budget.setDescription(request.getDescription());
        budget.setUser(user.get());

        budgetRepository.save(budget);

        return "Budget Added Successfully";
    }

    public List<Budget> getMyBudget() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        return budgetRepository.findByUser(user.get());
    }

    public BudgetStatusResponse getBudgetStatus() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new BudgetStatusResponse();
        }

        Optional<Budget> latestBudget =
                budgetRepository.findFirstByUserOrderByIdDesc(user.get());

        if (latestBudget.isEmpty()) {
            return new BudgetStatusResponse();
        }

        Budget budget = latestBudget.get();

        List<Expense> expenses =
                expenseRepository.findByUser(user.get());

        double spentAmount = 0;

        for (Expense expense : expenses) {
            spentAmount += expense.getAmount();
        }

        double remainingBudget =
                budget.getMonthlyBudget() - spentAmount;

        BudgetStatusResponse response = new BudgetStatusResponse();

        response.setMonthlyBudget(budget.getMonthlyBudget());
        response.setSpentAmount(spentAmount);
        response.setRemainingBudget(remainingBudget);

        return response;
    }

    public String deleteBudget(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Budget budget = budgetRepository.findByIdAndUser(id, user.get())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Budget not found or access denied"));

        budgetRepository.delete(budget);

        return "Budget deleted successfully";
    }
}
