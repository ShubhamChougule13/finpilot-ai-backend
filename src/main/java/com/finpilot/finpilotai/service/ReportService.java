package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.ExpenseCategoryReportResponse;
import com.finpilot.finpilotai.dto.TopCategoryResponse;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.ExpenseRepository;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ReportService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Existing all-time category report.
    public List<ExpenseCategoryReportResponse>
    getCategoryReport() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        List<Expense> expenses =
                expenseRepository.findByUser(user.get());

        return aggregateCategories(expenses);
    }

    // Existing all-time top category.
    public TopCategoryResponse getTopCategory() {

        return findTopCategory(getCategoryReport());
    }

    // New: top spending category for the current calendar month.
    public TopCategoryResponse getMonthlyTopCategory() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new TopCategoryResponse();
        }

        LocalDate startDate =
                LocalDate.now().withDayOfMonth(1);

        LocalDate endDate =
                startDate.plusMonths(1);

        List<Expense> monthlyExpenses =
                expenseRepository
                        .findByUserAndExpenseDateGreaterThanEqualAndExpenseDateLessThan(
                                user.get(),
                                startDate,
                                endDate
                        );

        List<ExpenseCategoryReportResponse> reports =
                aggregateCategories(monthlyExpenses);

        return findTopCategory(reports);
    }

    // Shared category aggregation logic.
    private List<ExpenseCategoryReportResponse>
    aggregateCategories(List<Expense> expenses) {

        Map<String, Double> categoryTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();

            if (category == null || category.isBlank()) {
                continue;
            }

            double amount = expense.getAmount() != null
                    ? expense.getAmount()
                    : 0.0;

            categoryTotals.put(
                    category,
                    categoryTotals.getOrDefault(category, 0.0)
                            + amount
            );
        }

        List<ExpenseCategoryReportResponse> responseList =
                new ArrayList<>();

        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            ExpenseCategoryReportResponse response =
                    new ExpenseCategoryReportResponse();

            response.setCategory(entry.getKey());
            response.setTotalAmount(entry.getValue());

            responseList.add(response);
        }

        return responseList;
    }

    // Shared top-category selection logic.
    private TopCategoryResponse findTopCategory(
            List<ExpenseCategoryReportResponse> reports) {

        if (reports.isEmpty()) {
            return new TopCategoryResponse();
        }

        ExpenseCategoryReportResponse topCategory =
                reports.get(0);

        for (ExpenseCategoryReportResponse report : reports) {

            if (report.getTotalAmount() != null
                    && (topCategory.getTotalAmount() == null
                    || report.getTotalAmount()
                    > topCategory.getTotalAmount())) {

                topCategory = report;
            }
        }

        TopCategoryResponse response =
                new TopCategoryResponse();

        response.setCategory(topCategory.getCategory());
        response.setAmount(topCategory.getTotalAmount());

        return response;
    }
}