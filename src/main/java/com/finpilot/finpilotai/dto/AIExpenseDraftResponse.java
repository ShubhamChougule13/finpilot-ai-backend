
package com.finpilot.finpilotai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIExpenseDraftResponse {

    private String title;

    private Double amount;

    private String category;

    private String description;

    private String message;

    private boolean readyForReview;
}
