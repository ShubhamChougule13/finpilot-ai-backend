
        package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.ExpenseRequest;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.ExpenseRepository;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public String addExpense(ExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());

        // Use the supplied date, or today's date if omitted.
        expense.setExpenseDate(
                request.getExpenseDate() != null
                        ? request.getExpenseDate()
                        : LocalDate.now()
        );

        expense.setUser(user.get());

        expenseRepository.save(expense);

        return "Expense Added Successfully";
    }

    public List<Expense> getMyExpenses() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        return expenseRepository.findByUser(user.get());
    }

    public String deleteExpense(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Expense> expense =
                expenseRepository.findById(id);

        if (expense.isEmpty()) {
            return "Expense Not Found";
        }

        if (expense.get().getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        expenseRepository.deleteById(id);

        return "Expense Deleted Successfully";
    }

    public String updateExpense(
            Long id,
            ExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Expense> expense =
                expenseRepository.findById(id);

        if (expense.isEmpty()) {
            return "Expense Not Found";
        }

        Expense existingExpense = expense.get();

        if (existingExpense.getUser() == null) {
            return "Expense Owner Not Found";
        }

        if (existingExpense.getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        existingExpense.setTitle(
                request.getTitle());

        existingExpense.setAmount(
                request.getAmount());

        existingExpense.setCategory(
                request.getCategory());

        existingExpense.setDescription(
                request.getDescription());

        // Change the date only when one is supplied.
        if (request.getExpenseDate() != null) {
            existingExpense.setExpenseDate(
                    request.getExpenseDate());
        }

        expenseRepository.save(existingExpense);

        return "Expense Updated Successfully";
    }
}
