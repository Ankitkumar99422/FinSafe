package com.finsafe.dto;

import java.math.BigDecimal;
import java.util.List;

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

    public DashboardResponse() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public BigDecimal getMonthlyEmi() { return monthlyEmi; }
    public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

    public BigDecimal getMonthlySurplus() { return monthlySurplus; }
    public void setMonthlySurplus(BigDecimal monthlySurplus) { this.monthlySurplus = monthlySurplus; }

    public List<GoalSummary> getGoals() { return goals; }
    public void setGoals(List<GoalSummary> goals) { this.goals = goals; }

    public List<EmiSummary> getEmis() { return emis; }
    public void setEmis(List<EmiSummary> emis) { this.emis = emis; }

    public List<RecentDecisionSummary> getRecentDecisions() { return recentDecisions; }
    public void setRecentDecisions(List<RecentDecisionSummary> recentDecisions) { this.recentDecisions = recentDecisions; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder currentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; return this; }
        public Builder monthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; return this; }
        public Builder monthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; return this; }
        public Builder monthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; return this; }
        public Builder monthlySurplus(BigDecimal monthlySurplus) { this.monthlySurplus = monthlySurplus; return this; }
        public Builder goals(List<GoalSummary> goals) { this.goals = goals; return this; }
        public Builder emis(List<EmiSummary> emis) { this.emis = emis; return this; }
        public Builder recentDecisions(List<RecentDecisionSummary> recentDecisions) { this.recentDecisions = recentDecisions; return this; }

        public DashboardResponse build() {
            DashboardResponse resp = new DashboardResponse();
            resp.setFullName(this.fullName);
            resp.setEmail(this.email);
            resp.setCurrentBalance(this.currentBalance);
            resp.setMonthlyIncome(this.monthlyIncome);
            resp.setMonthlyExpenses(this.monthlyExpenses);
            resp.setMonthlyEmi(this.monthlyEmi);
            resp.setMonthlySurplus(this.monthlySurplus);
            resp.setGoals(this.goals);
            resp.setEmis(this.emis);
            resp.setRecentDecisions(this.recentDecisions);
            return resp;
        }
    }

    public static class GoalSummary {
        private Long id;
        private String name;
        private BigDecimal targetAmount;
        private BigDecimal savedAmount;
        private String priority;
        private String targetDate;

        public GoalSummary() {}
        public GoalSummary(Long id, String name, BigDecimal targetAmount, BigDecimal savedAmount, String priority, String targetDate) {
            this.id = id;
            this.name = name;
            this.targetAmount = targetAmount;
            this.savedAmount = savedAmount;
            this.priority = priority;
            this.targetDate = targetDate;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public BigDecimal getTargetAmount() { return targetAmount; }
        public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
        public BigDecimal getSavedAmount() { return savedAmount; }
        public void setSavedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        public String getTargetDate() { return targetDate; }
        public void setTargetDate(String targetDate) { this.targetDate = targetDate; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String name;
            private BigDecimal targetAmount;
            private BigDecimal savedAmount;
            private String priority;
            private String targetDate;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder targetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; return this; }
            public Builder savedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; return this; }
            public Builder priority(String priority) { this.priority = priority; return this; }
            public Builder targetDate(String targetDate) { this.targetDate = targetDate; return this; }

            public GoalSummary build() {
                return new GoalSummary(id, name, targetAmount, savedAmount, priority, targetDate);
            }
        }
    }

    public static class EmiSummary {
        private Long id;
        private String name;
        private BigDecimal monthlyAmount;
        private Integer remainingMonths;

        public EmiSummary() {}
        public EmiSummary(Long id, String name, BigDecimal monthlyAmount, Integer remainingMonths) {
            this.id = id;
            this.name = name;
            this.monthlyAmount = monthlyAmount;
            this.remainingMonths = remainingMonths;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public BigDecimal getMonthlyAmount() { return monthlyAmount; }
        public void setMonthlyAmount(BigDecimal monthlyAmount) { this.monthlyAmount = monthlyAmount; }
        public Integer getRemainingMonths() { return remainingMonths; }
        public void setRemainingMonths(Integer remainingMonths) { this.remainingMonths = remainingMonths; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String name;
            private BigDecimal monthlyAmount;
            private Integer remainingMonths;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder monthlyAmount(BigDecimal monthlyAmount) { this.monthlyAmount = monthlyAmount; return this; }
            public Builder remainingMonths(Integer remainingMonths) { this.remainingMonths = remainingMonths; return this; }

            public EmiSummary build() {
                return new EmiSummary(id, name, monthlyAmount, remainingMonths);
            }
        }
    }

    public static class RecentDecisionSummary {
        private Long id;
        private String item;
        private BigDecimal price;
        private String recommendation;
        private String validatorStatus;
        private String createdAt;

        public RecentDecisionSummary() {}
        public RecentDecisionSummary(Long id, String item, BigDecimal price, String recommendation, String validatorStatus, String createdAt) {
            this.id = id;
            this.item = item;
            this.price = price;
            this.recommendation = recommendation;
            this.validatorStatus = validatorStatus;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getItem() { return item; }
        public void setItem(String item) { this.item = item; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
        public String getValidatorStatus() { return validatorStatus; }
        public void setValidatorStatus(String validatorStatus) { this.validatorStatus = validatorStatus; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String item;
            private BigDecimal price;
            private String recommendation;
            private String validatorStatus;
            private String createdAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder item(String item) { this.item = item; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }
            public Builder recommendation(String recommendation) { this.recommendation = recommendation; return this; }
            public Builder validatorStatus(String validatorStatus) { this.validatorStatus = validatorStatus; return this; }
            public Builder createdAt(String createdAt) { this.createdAt = createdAt; return this; }

            public RecentDecisionSummary build() {
                return new RecentDecisionSummary(id, item, price, recommendation, validatorStatus, createdAt);
            }
        }
    }
}
