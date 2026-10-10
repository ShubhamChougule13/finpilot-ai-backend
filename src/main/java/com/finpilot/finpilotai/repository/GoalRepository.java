package com.finpilot.finpilotai.repository;

import com.finpilot.finpilotai.entity.Goal;
import com.finpilot.finpilotai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository
        extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);

    Optional<Goal> findById(Long id);
}