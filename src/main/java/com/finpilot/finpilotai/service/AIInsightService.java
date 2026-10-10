package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.AIInsightResponse;
import com.finpilot.finpilotai.entity.Budget;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.Goal;
import com.finpilot.finpilotai.entity.Income;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.BudgetRepository;
import com.finpilot.finpilotai.repository.ExpenseRepository;
import com.finpilot.finpilotai.repository.GoalRepository;
import com.finpilot.finpilotai.repository.IncomeRepository;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AIInsightService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public AIInsightService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            GoalRepository goalRepository,
            BudgetRepository budgetRepository,
            UserRepository userRepository) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.goalRepository = goalRepository;
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    public AIInsightResponse getInsights() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new AIInsightResponse();
        }

        List<Income> incomes =
                incomeRepository.findByUser(user.get());

        List<Expense> expenses =
                expenseRepository.findByUser(user.get());

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double savings =
                totalIncome - totalExpense;

        double goalProgress = 0;

        List<Goal> goals =
                goalRepository.findByUser(user.get());

        if (!goals.isEmpty()) {

            Goal goal = goals.get(0);

            goalProgress =
                    (savings / goal.getTargetAmount()) * 100;
        }

        double remainingBudget = 0;

        List<Budget> budgets =
                budgetRepository.findByUser(user.get());

        if (!budgets.isEmpty()) {

            Budget budget = budgets.get(0);

            remainingBudget =
                    budget.getMonthlyBudget()
                            - totalExpense;
        }

        AIInsightResponse response =
                new AIInsightResponse();

        response.setSavings(savings);
        response.setGoalProgress(goalProgress);
        response.setRemainingBudget(remainingBudget);

        String message =
                "Excellent! You have saved ₹"
                        + savings
                        + " and completed "
                        + String.format("%.2f", goalProgress)
                        + "% of your goal. "
                        + "You still have ₹"
                        + remainingBudget
                        + " remaining in your budget.";

        response.setMessage(message);

        return response;
    }
}