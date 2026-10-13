
package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.DashboardResponse;
import com.finpilot.finpilotai.dto.EnhancedDashboardResponse;
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
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final BudgetRepository budgetRepository;

    public DashboardService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            GoalRepository goalRepository,
            BudgetRepository budgetRepository) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.budgetRepository = budgetRepository;
    }

    public DashboardResponse getDashboard() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new DashboardResponse();
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

        DashboardResponse response = new DashboardResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setSavings(totalIncome - totalExpense);

        return response;
    }

    public EnhancedDashboardResponse getEnhancedDashboard() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new EnhancedDashboardResponse();
        }

        User currentUser = user.get();

        List<Income> incomes =
                incomeRepository.findByUser(currentUser);

        List<Expense> expenses =
                expenseRepository.findByUser(currentUser);

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double savings = totalIncome - totalExpense;

        double goalProgress = 0;
        double monthlyBudget = 0;
        double remainingBudget = 0;

        List<Goal> goals =
                goalRepository.findByUser(currentUser);

        if (!goals.isEmpty()) {

            Goal goal = goals.get(0);

            if (goal.getTargetAmount() != null
                    && goal.getTargetAmount() > 0) {

                goalProgress =
                        (savings / goal.getTargetAmount()) * 100;

                goalProgress = Math.min(goalProgress, 100);
            }
        }

        Optional<Budget> latestBudget =
                budgetRepository.findFirstByUserOrderByIdDesc(currentUser);

        if (latestBudget.isPresent()) {

            Budget budget = latestBudget.get();

            monthlyBudget = budget.getMonthlyBudget();

            remainingBudget = monthlyBudget - totalExpense;
        }

        EnhancedDashboardResponse response =
                new EnhancedDashboardResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setSavings(savings);
        response.setGoalProgress(goalProgress);
        response.setMonthlyBudget(monthlyBudget);
        response.setRemainingBudget(remainingBudget);

        return response;
    }
}
