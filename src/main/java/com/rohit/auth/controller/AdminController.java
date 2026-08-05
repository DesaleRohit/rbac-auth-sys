package com.rohit.auth.controller;

import com.rohit.auth.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ApiResponse adminDashboard(Authentication authentication) {
        return new ApiResponse(
                "Welcome, Admin " + authentication.getName() + "! You have full access to the dashboard."
        );
    }
}