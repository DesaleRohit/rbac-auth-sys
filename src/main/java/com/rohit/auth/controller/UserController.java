package com.rohit.auth.controller;

import com.rohit.auth.dto.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public ApiResponse userProfile(Authentication authentication) {
        return new ApiResponse(
                "Welcome, " + authentication.getName() + "! This is your profile page."
        );
    }
}