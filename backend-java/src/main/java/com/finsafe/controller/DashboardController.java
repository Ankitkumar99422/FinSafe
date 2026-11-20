package com.finsafe.controller;

import com.finsafe.dto.DashboardResponse;
import com.finsafe.entity.User;
import com.finsafe.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints for fetching live financial metrics, balances, and monthly surplus")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get current authenticated user dashboard metrics")
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(dashboardService.getDashboardData(userId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get dashboard metrics for a specific user ID (Demo/Testing)")
    public ResponseEntity<DashboardResponse> getDashboardByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboardData(userId));
    }
}
