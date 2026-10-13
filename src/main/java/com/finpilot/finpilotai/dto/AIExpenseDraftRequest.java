
package com.finpilot.finpilotai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIExpenseDraftRequest {

    @NotBlank(message = "Message is required")
    private String message;
}
