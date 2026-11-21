package com.finsafe.controller;

import com.finsafe.dto.PurchaseDtos.*;
import com.finsafe.entity.User;
import com.finsafe.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchase Analyzer", description = "Core decision engine evaluating purchases with V1–V9 Safety Validator")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze planned purchase and generate candidate + validated safety verdict")
    public ResponseEntity<AnalyzeResponse> analyzePurchase(
            @AuthenticationPrincipal User user,
            @RequestBody AnalyzeRequest request) {
        Long userId = (user != null) ? user.getId() : 1L;
        return ResponseEntity.ok(purchaseService.analyzePurchase(userId, request));
    }
}
