
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

import java.time.LocalDate;
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

    // Existing dashboard: all-time totals remain unchanged.
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
            totalIncome += income.getAmount() != null
                    ? income.getAmount() : 0.0;
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount() != null
                    ? expense.getAmount() : 0.0;
        }

        DashboardResponse response = new DashboardResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setSavings(totalIncome - totalExpense);

        return response;
    }

    // Enhanced dashboard: monthly income and expenses.
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

        // Current calendar month: start inclusive, next month exclusive.
        LocalDate startDate =
                LocalDate.now().withDayOfMonth(1);

        LocalDate endDate =
                startDate.plusMonths(1);

        List<Income> monthlyIncomes =
                incomeRepository
                        .findByUserAndIncomeDateGreaterThanEqualAndIncomeDateLessThan(
                                currentUser,
                                startDate,
                                endDate
                        );

        List<Expense> monthlyExpenses =
                expenseRepository
                        .findByUserAndExpenseDateGreaterThanEqualAndExpenseDateLessThan(
                                currentUser,
                                startDate,
                                endDate
                        );

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : monthlyIncomes) {
            totalIncome += income.getAmount() != null
                    ? income.getAmount() : 0.0;
        }

        for (Expense expense : monthlyExpenses) {
            totalExpense += expense.getAmount() != null
                    ? expense.getAmount() : 0.0;
        }

        double savings = totalIncome - totalExpense;

        // Preserve the existing goal-progress calculation.
        double goalProgress = 0;

        List<Goal> goals =
                goalRepository.findByUser(currentUser);

        if (!goals.isEmpty()) {

            Goal goal = goals.get(0);

            double savedAmount =
                    goal.getSavedAmount() != null
                            ? goal.getSavedAmount()
                            : 0.0;

            if (goal.getTargetAmount() != null
                    && goal.getTargetAmount() > 0) {

                goalProgress =
                        (savedAmount / goal.getTargetAmount()) * 100;

                goalProgress = Math.min(goalProgress, 100);
            }
        }

        // Compare this month's expenses against the monthly budget.
        double monthlyBudget = 0;
        double remainingBudget = 0;

        Optional<Budget> latestBudget =
                budgetRepository.findFirstByUserOrderByIdDesc(currentUser);

        if (latestBudget.isPresent()) {

            Budget budget = latestBudget.get();

            monthlyBudget = budget.getMonthlyBudget() != null
                    ? budget.getMonthlyBudget()
                    : 0.0;

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
