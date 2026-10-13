
package com.finpilot.finpilotai.repository;

import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    Optional<Expense> findById(Long id);

    // Find expenses within a date range for a specific user.
    List<Expense> findByUserAndExpenseDateGreaterThanEqualAndExpenseDateLessThan(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );
}
