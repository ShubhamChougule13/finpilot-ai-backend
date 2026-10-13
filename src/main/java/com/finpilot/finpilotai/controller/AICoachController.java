
package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.AIExpenseDraftRequest;
import com.finpilot.finpilotai.dto.AIExpenseDraftResponse;
import com.finpilot.finpilotai.service.GeminiSdkService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-coach")
public class AICoachController {

    private final GeminiSdkService geminiSdkService;

    public AICoachController(GeminiSdkService geminiSdkService) {
        this.geminiSdkService = geminiSdkService;
    }

    @PostMapping("/expense-draft")
    public AIExpenseDraftResponse createExpenseDraft(
            @Valid @RequestBody AIExpenseDraftRequest request) {

        return geminiSdkService.createExpenseDraft(request);
    }
}
