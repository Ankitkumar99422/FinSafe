package com.finsafe.controller;

import com.finsafe.entity.Goal;
import com.finsafe.entity.User;
import com.finsafe.repository.GoalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Endpoints for managing protected savings goals")
public class GoalController {

    private final GoalRepository goalRepository;

    @GetMapping
    @Operation(summary = "List all active savings goals")
    public ResponseEntity<List<Goal>> getGoals(@AuthenticationPrincipal User user) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(goalRepository.findByUserIdOrderByPriorityAsc(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new savings goal")
    public ResponseEntity<Goal> createGoal(
            @AuthenticationPrincipal User user,
            @RequestBody Goal goal) {
        Long userId = (user != null) ? user.getId() : 1L;
        goal.setUserId(userId);
        if (goal.getStatus() == null) goal.setStatus("IN_PROGRESS");
        if (goal.getPriority() == null) goal.setPriority("HIGH");
        return ResponseEntity.ok(goalRepository.save(goal));
    }
}
