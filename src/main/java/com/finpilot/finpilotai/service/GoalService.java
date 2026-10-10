package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.GoalProgressResponse;
import com.finpilot.finpilotai.dto.GoalRequest;
import com.finpilot.finpilotai.entity.Expense;
import com.finpilot.finpilotai.entity.Goal;
import com.finpilot.finpilotai.entity.Income;
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
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Goal goal = new Goal();

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setDescription(request.getDescription());

        goal.setUser(user.get());

        goalRepository.save(goal);

        return "Goal Added Successfully";
    }

    public List<Goal> getMyGoals() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        return goalRepository.findByUser(user.get());
    }

    public String updateGoal(
            Long id,
            GoalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Goal> goal =
                goalRepository.findById(id);

        if (goal.isEmpty()) {
            return "Goal Not Found";
        }

        Goal existingGoal = goal.get();

        if (existingGoal.getUser() == null) {
            return "Goal Owner Not Found";
        }

        if (existingGoal.getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        existingGoal.setGoalName(
                request.getGoalName());

        existingGoal.setTargetAmount(
                request.getTargetAmount());

        existingGoal.setDescription(
                request.getDescription());

        goalRepository.save(existingGoal);

        return "Goal Updated Successfully";
    }

    public String deleteGoal(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Goal> goal =
                goalRepository.findById(id);

        if (goal.isEmpty()) {
            return "Goal Not Found";
        }

        Goal existingGoal = goal.get();

        if (existingGoal.getUser() == null) {
            return "Goal Owner Not Found";
        }

        if (existingGoal.getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        goalRepository.deleteById(id);

        return "Goal Deleted Successfully";
    }

    public GoalProgressResponse getGoalProgress() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new GoalProgressResponse();
        }

        List<Goal> goals =
                goalRepository.findByUser(user.get());

        if (goals.isEmpty()) {
            return new GoalProgressResponse();
        }

        Goal goal = goals.get(0);

        List<Income> incomes =
                incomeRepository.findByUser(user.get());

        List<Expense> expenses =
                expenseRepository.findByUser(user.get());

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double currentSavings =
                totalIncome - totalExpense;

        double progress =
                (currentSavings / goal.getTargetAmount())
                        * 100;

        GoalProgressResponse response =
                new GoalProgressResponse();

        response.setGoalName(
                goal.getGoalName());

        response.setTargetAmount(
                goal.getTargetAmount());

        response.setCurrentSavings(
                currentSavings);

        response.setProgress(
                progress);

        return response;
    }
}