package com.finpilot.finpilotai.repository;

import com.finpilot.finpilotai.entity.Income;
import com.finpilot.finpilotai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository
        extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    Optional<Income> findById(Long id);
}