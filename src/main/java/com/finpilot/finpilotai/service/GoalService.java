
        package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.GoalProgressResponse;
import com.finpilot.finpilotai.dto.GoalRequest;
import com.finpilot.finpilotai.entity.Goal;
import com.finpilot.finpilotai.entity.User;
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
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public GoalService(
            GoalRepository goalRepository,
            UserRepository userRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository) {

        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    public String addGoal(GoalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        String validationError = validateAmounts(request);
        if (validationError != null) {
            return validationError;
        }

        Goal goal = new Goal();

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());

        goal.setSavedAmount(
                request.getSavedAmount() != null
                        ? request.getSavedAmount()
                        : 0.0
        );

        goal.setDescription(request.getDescription());
        goal.setUser(user.get());

        goalRepository.save(goal);

        return "Goal Added Successfully";
    }

    public List<Goal> getMyGoals() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        List<Goal> goals = goalRepository.findByUser(user.get());

        // Handle existing database records with NULL saved amounts.
        for (Goal goal : goals) {
            if (goal.getSavedAmount() == null) {
                goal.setSavedAmount(0.0);
            }
        }

        return goals;
    }

    public String updateGoal(Long id, GoalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Goal> goal = goalRepository.findById(id);

        if (goal.isEmpty()) {
            return "Goal Not Found";
        }

        Goal existingGoal = goal.get();

        if (existingGoal.getUser() == null) {
            return "Goal Owner Not Found";
        }

        if (existingGoal.getUser().getId() != user.get().getId()) {
            return "Unauthorized Access";
        }

        String validationError = validateAmounts(request);
        if (validationError != null) {
            return validationError;
        }

        existingGoal.setGoalName(request.getGoalName());
        existingGoal.setTargetAmount(request.getTargetAmount());

        if (request.getSavedAmount() != null) {
            existingGoal.setSavedAmount(request.getSavedAmount());
        } else if (existingGoal.getSavedAmount() == null) {
            existingGoal.setSavedAmount(0.0);
        }

        existingGoal.setDescription(request.getDescription());

        goalRepository.save(existingGoal);

        return "Goal Updated Successfully";
    }

    public String deleteGoal(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Goal> goal = goalRepository.findById(id);

        if (goal.isEmpty()) {
            return "Goal Not Found";
        }

        Goal existingGoal = goal.get();

        if (existingGoal.getUser() == null) {
            return "Goal Owner Not Found";
        }

        if (existingGoal.getUser().getId() != user.get().getId()) {
            return "Unauthorized Access";
        }

        goalRepository.deleteById(id);

        return "Goal Deleted Successfully";
    }

    private String validateAmounts(GoalRequest request) {

        if (request.getTargetAmount() == null
                || request.getTargetAmount() <= 0) {
            return "Target amount must be greater than zero";
        }

        if (request.getSavedAmount() != null
                && request.getSavedAmount() < 0) {
            return "Saved amount cannot be negative";
        }

        double savedAmount = request.getSavedAmount() != null
                ? request.getSavedAmount()
                : 0.0;

        if (savedAmount > request.getTargetAmount()) {
            return "Saved amount cannot exceed target amount";
        }

        return null;
    }

    public GoalProgressResponse getGoalProgress() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new GoalProgressResponse();
        }

        List<Goal> goals = goalRepository.findByUser(user.get());

        if (goals.isEmpty()) {
            return new GoalProgressResponse();
        }

        Goal goal = goals.get(0);

        double savedAmount = goal.getSavedAmount() != null
                ? goal.getSavedAmount()
                : 0.0;

        double targetAmount = goal.getTargetAmount() != null
                ? goal.getTargetAmount()
                : 0.0;

        double progress = targetAmount > 0
                ? (savedAmount / targetAmount) * 100
                : 0.0;

        GoalProgressResponse response = new GoalProgressResponse();

        response.setGoalName(goal.getGoalName());
        response.setTargetAmount(targetAmount);
        response.setCurrentSavings(savedAmount);
        response.setProgress(progress);

        return response;
    }
}
