package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalProgressResponse {

    private String goalName;

    private Double targetAmount;

    private Double currentSavings;

    private Double progress;
}