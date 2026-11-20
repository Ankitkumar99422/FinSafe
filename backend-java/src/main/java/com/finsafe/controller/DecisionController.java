package com.finsafe.controller;

import com.finsafe.entity.Decision;
import com.finsafe.entity.User;
import com.finsafe.repository.DecisionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decisions")
@RequiredArgsConstructor
@Tag(name = "Decisions & History", description = "Endpoints for reviewing past purchase decisions and Safety Validator audits")
public class DecisionController {

    private final DecisionRepository decisionRepository;

    @GetMapping
    @Operation(summary = "List all purchase decisions for current user")
    public ResponseEntity<List<Decision>> getDecisions(@AuthenticationPrincipal User user) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(decisionRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @GetMapping("/{decisionId}")
    @Operation(summary = "Get detailed audit breakdown for a specific decision")
    public ResponseEntity<Decision> getDecisionById(
            @PathVariable Long decisionId,
            @AuthenticationPrincipal User user) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new IllegalArgumentException("Decision not found with id: " + decisionId));
        return ResponseEntity.ok(decision);
    }
}
