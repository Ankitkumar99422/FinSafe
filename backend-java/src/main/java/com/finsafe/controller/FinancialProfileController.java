package com.finsafe.controller;

import com.finsafe.dto.FinancialProfileDtos.*;
import com.finsafe.entity.User;
import com.finsafe.service.FinancialProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profile")
@Tag(name = "Financial Profile", description = "Endpoints for managing user income, balance, expenses, and onboarding profile")
public class FinancialProfileController {

    private final FinancialProfileService profileService;

    public FinancialProfileController(FinancialProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @Operation(summary = "Save or update complete financial profile (Onboarding)")
    public ResponseEntity<ProfileResponse> saveProfile(
            @AuthenticationPrincipal User user,
            @RequestBody ProfileSaveRequest request) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(profileService.saveProfile(userId, request));
    }

    @GetMapping
    @Operation(summary = "Get user financial profile and baseline figures")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(profileService.getProfile(userId));
    }
}
