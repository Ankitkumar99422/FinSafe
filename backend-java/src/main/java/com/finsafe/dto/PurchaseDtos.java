package com.finsafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyzeRequest {
        private String item;
        private BigDecimal price;
        private String paymentMethod; // 'UPFRONT' or 'EMI'
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalyzeResponse {
        private Long decisionId;
        private String item;
        private BigDecimal price;
        private String finalRecommendation; // 'Buy Now', 'EMI', 'Wait', 'Not Recommended'
        private String candidateRecommendation;
        private String validatorStatus; // 'PASS', 'MODIFY', 'REJECT'
        private BigDecimal balanceBeforePurchase;
        private BigDecimal projectedBalanceTypical;
        private BigDecimal projectedBalanceBadCase;
        private BigDecimal maxSafePurchase;
        private List<String> triggeredRules;
        private List<String> reasons;
        private String explanation;
    }
}
