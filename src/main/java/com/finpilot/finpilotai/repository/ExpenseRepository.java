package com.finpilot.finpilotai.repository;

import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
    Optional<Expense> findById(Long id);
}