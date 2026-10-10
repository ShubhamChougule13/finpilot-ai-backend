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

        Map<String, Double> categoryTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            categoryTotals.put(
                    expense.getCategory(),

                    categoryTotals.getOrDefault(
                            expense.getCategory(),
                            0.0
                    ) + expense.getAmount()
            );
        }

        List<ExpenseCategoryReportResponse>
                responseList = new ArrayList<>();

        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            ExpenseCategoryReportResponse response =
                    new ExpenseCategoryReportResponse();

            response.setCategory(
                    entry.getKey());

            response.setTotalAmount(
                    entry.getValue());

            responseList.add(response);
        }

        return responseList;
    }

    public TopCategoryResponse getTopCategory() {

        List<ExpenseCategoryReportResponse> reports =
                getCategoryReport();

        if (reports.isEmpty()) {
            return new TopCategoryResponse();
        }

        ExpenseCategoryReportResponse topCategory =
                reports.get(0);

        for (ExpenseCategoryReportResponse report : reports) {

            if (report.getTotalAmount()
                    > topCategory.getTotalAmount()) {

                topCategory = report;
            }
        }

        TopCategoryResponse response =
                new TopCategoryResponse();

        response.setCategory(
                topCategory.getCategory());

        response.setAmount(
                topCategory.getTotalAmount());

        return response;
    }
}