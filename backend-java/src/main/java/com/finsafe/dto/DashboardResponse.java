package com.finsafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private String fullName;
    private String email;
    private BigDecimal currentBalance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal monthlyEmi;
    private BigDecimal monthlySurplus;
    private List<GoalSummary> goals;
    private List<EmiSummary> emis;
    private List<RecentDecisionSummary> recentDecisions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GoalSummary {
        private Long id;
        private String name;
        private BigDecimal targetAmount;
        private BigDecimal savedAmount;
        private String priority;
        private String targetDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmiSummary {
        private Long id;
        private String name;
        private BigDecimal monthlyAmount;
        private Integer remainingMonths;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentDecisionSummary {
        private Long id;
        private String item;
        private BigDecimal price;
        private String recommendation;
        private String validatorStatus;
        private String createdAt;
    }
}
