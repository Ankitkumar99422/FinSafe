package com.finsafe.dto;

import java.math.BigDecimal;
import java.util.List;

public class FinancialProfileDtos {

    public static class ProfileSaveRequest {
        private Integer age;
        private String occupation;
        private String employmentStatus;
        private BigDecimal monthlyIncome;
        private BigDecimal currentBalance;
        private BigDecimal monthlyExpenses;
        private BigDecimal monthlyEmi;
        private List<GoalItem> goals;

        public ProfileSaveRequest() {}

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getOccupation() { return occupation; }
        public void setOccupation(String occupation) { this.occupation = occupation; }

        public String getEmploymentStatus() { return employmentStatus; }
        public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

        public BigDecimal getMonthlyIncome() { return monthlyIncome; }
        public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

        public BigDecimal getCurrentBalance() { return currentBalance; }
        public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

        public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
        public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

        public BigDecimal getMonthlyEmi() { return monthlyEmi; }
        public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

        public List<GoalItem> getGoals() { return goals; }
        public void setGoals(List<GoalItem> goals) { this.goals = goals; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Integer age;
            private String occupation;
            private String employmentStatus;
            private BigDecimal monthlyIncome;
            private BigDecimal currentBalance;
            private BigDecimal monthlyExpenses;
            private BigDecimal monthlyEmi;
            private List<GoalItem> goals;

            public Builder age(Integer age) { this.age = age; return this; }
            public Builder occupation(String occupation) { this.occupation = occupation; return this; }
            public Builder employmentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; return this; }
            public Builder monthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; return this; }
            public Builder currentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; return this; }
            public Builder monthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; return this; }
            public Builder monthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; return this; }
            public Builder goals(List<GoalItem> goals) { this.goals = goals; return this; }

            public ProfileSaveRequest build() {
                ProfileSaveRequest req = new ProfileSaveRequest();
                req.setAge(this.age);
                req.setOccupation(this.occupation);
                req.setEmploymentStatus(this.employmentStatus);
                req.setMonthlyIncome(this.monthlyIncome);
                req.setCurrentBalance(this.currentBalance);
                req.setMonthlyExpenses(this.monthlyExpenses);
                req.setMonthlyEmi(this.monthlyEmi);
                req.setGoals(this.goals);
                return req;
            }
        }
    }

    public static class GoalItem {
        private String goalName;
        private BigDecimal targetAmount;
        private BigDecimal savedAmount;
        private String targetDate;
        private String priority;

        public GoalItem() {}

        public String getGoalName() { return goalName; }
        public void setGoalName(String goalName) { this.goalName = goalName; }

        public BigDecimal getTargetAmount() { return targetAmount; }
        public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

        public BigDecimal getSavedAmount() { return savedAmount; }
        public void setSavedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; }

        public String getTargetDate() { return targetDate; }
        public void setTargetDate(String targetDate) { this.targetDate = targetDate; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private String goalName;
            private BigDecimal targetAmount;
            private BigDecimal savedAmount;
            private String targetDate;
            private String priority;

            public Builder goalName(String goalName) { this.goalName = goalName; return this; }
            public Builder targetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; return this; }
            public Builder savedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; return this; }
            public Builder targetDate(String targetDate) { this.targetDate = targetDate; return this; }
            public Builder priority(String priority) { this.priority = priority; return this; }

            public GoalItem build() {
                GoalItem item = new GoalItem();
                item.setGoalName(this.goalName);
                item.setTargetAmount(this.targetAmount);
                item.setSavedAmount(this.savedAmount);
                item.setTargetDate(this.targetDate);
                item.setPriority(this.priority);
                return item;
            }
        }
    }

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

        public ProfileResponse() {}

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getOccupation() { return occupation; }
        public void setOccupation(String occupation) { this.occupation = occupation; }

        public String getEmploymentStatus() { return employmentStatus; }
        public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

        public BigDecimal getCurrentBalance() { return currentBalance; }
        public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

        public BigDecimal getMonthlyIncome() { return monthlyIncome; }
        public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

        public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
        public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

        public BigDecimal getMonthlyEmi() { return monthlyEmi; }
        public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

        public List<GoalItem> getGoals() { return goals; }
        public void setGoals(List<GoalItem> goals) { this.goals = goals; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
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

            public Builder userId(Long userId) { this.userId = userId; return this; }
            public Builder fullName(String fullName) { this.fullName = fullName; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder age(Integer age) { this.age = age; return this; }
            public Builder occupation(String occupation) { this.occupation = occupation; return this; }
            public Builder employmentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; return this; }
            public Builder currentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; return this; }
            public Builder monthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; return this; }
            public Builder monthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; return this; }
            public Builder monthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; return this; }
            public Builder goals(List<GoalItem> goals) { this.goals = goals; return this; }

            public ProfileResponse build() {
                ProfileResponse resp = new ProfileResponse();
                resp.setUserId(this.userId);
                resp.setFullName(this.fullName);
                resp.setEmail(this.email);
                resp.setAge(this.age);
                resp.setOccupation(this.occupation);
                resp.setEmploymentStatus(this.employmentStatus);
                resp.setCurrentBalance(this.currentBalance);
                resp.setMonthlyIncome(this.monthlyIncome);
                resp.setMonthlyExpenses(this.monthlyExpenses);
                resp.setMonthlyEmi(this.monthlyEmi);
                resp.setGoals(this.goals);
                return resp;
            }
        }
    }
}
