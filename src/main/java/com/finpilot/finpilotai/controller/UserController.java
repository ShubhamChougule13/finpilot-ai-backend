package com.finpilot.finpilotai.controller;

import com.finpilot.finpilotai.dto.RegisterRequest;
import com.finpilot.finpilotai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.finpilot.finpilotai.dto.LoginRequest;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegisterRequest request) {

        return userService.registerUser(request);

    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginRequest request) {

        return userService.loginUser(request);

    }

    @GetMapping("/test")
    public String test() {

        return "Protected API Accessed";

    }
}
