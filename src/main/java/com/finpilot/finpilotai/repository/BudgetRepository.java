
package com.finpilot.finpilotai.repository;

import com.finpilot.finpilotai.entity.Budget;
import com.finpilot.finpilotai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser(User user);

    Optional<Budget> findById(Long id);

    Optional<Budget> findByIdAndUser(Long id, User user);

    Optional<Budget> findFirstByUserOrderByIdDesc(User user);
}
