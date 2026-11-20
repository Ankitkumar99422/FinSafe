package com.finsafe.controller;

import com.finsafe.dto.DashboardResponse;
import com.finsafe.entity.User;
import com.finsafe.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        // Fallback to demo user 1 if not authenticated (for convenient testing)
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(dashboardService.getDashboardData(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DashboardResponse> getDashboardByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboardData(userId));
    }
}
