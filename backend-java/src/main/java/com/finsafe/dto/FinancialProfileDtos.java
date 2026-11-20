package com.finsafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class FinancialProfileDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileSaveRequest {
        private Integer age;
        private String occupation;
        private String employmentStatus;
        private BigDecimal monthlyIncome;
        private BigDecimal currentBalance;
        private BigDecimal monthlyExpenses;
        private BigDecimal monthlyEmi;
        private List<GoalItem> goals;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GoalItem {
        private String goalName;
        private BigDecimal targetAmount;
        private BigDecimal savedAmount;
        private String targetDate;
        private String priority;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileResponse {
        private Long userId;
        private String fullName;
        private String email;
        private Integer age;
        private String occupation;
        private String employmentStatus;
        private BigDecimal currentBalance;
        private BigDecimal monthlyIncome;
        private BigDecimal monthlyExpenses;
        private BigDecimal monthlyEmi;
        private List<GoalItem> goals;
    }
}
