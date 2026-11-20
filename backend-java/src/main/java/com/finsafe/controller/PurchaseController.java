package com.finsafe.controller;

import com.finsafe.dto.PurchaseDtos.*;
import com.finsafe.entity.User;
import com.finsafe.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyzePurchase(
            @AuthenticationPrincipal User user,
            @RequestBody AnalyzeRequest request) {
        Long userId = (user != null) ? user.getId() : 1L; // Defaults to demo user 1 if testing without login
        return ResponseEntity.ok(purchaseService.analyzePurchase(userId, request));
    }
}
