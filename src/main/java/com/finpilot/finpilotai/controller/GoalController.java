package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.GoalProgressResponse;
import com.finpilot.finpilotai.dto.GoalRequest;
import com.finpilot.finpilotai.entity.Goal;
import com.finpilot.finpilotai.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoalController {

    private final GoalService goalService;

    public GoalController(
            GoalService goalService) {

        this.goalService = goalService;
    }

    @PostMapping("/goals")
    public String addGoal(
            @Valid @RequestBody GoalRequest request) {

        return goalService.addGoal(request);
    }

    @GetMapping("/goals")
    public List<Goal> getMyGoals() {

        return goalService.getMyGoals();
    }

    @PutMapping("/goals/{id}")
    public String updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody GoalRequest request) {

        return goalService.updateGoal(
                id,
                request);
    }

    @DeleteMapping("/goals/{id}")
    public String deleteGoal(
            @PathVariable Long id) {

        return goalService.deleteGoal(id);
    }

    @GetMapping("/goals/progress")
    public GoalProgressResponse getGoalProgress() {

        return goalService.getGoalProgress();
    }
}