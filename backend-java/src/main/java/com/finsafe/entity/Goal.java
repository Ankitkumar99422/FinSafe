package com.finsafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goal_name", nullable = false)
    private String goalName;

    @Column(name = "target_amount", nullable = false)
    private BigDecimal targetAmount;

    @Column(name = "saved_amount", nullable = false)
    private BigDecimal savedAmount;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false)
    private String priority; // 'HIGH', 'MEDIUM', 'LOW'

    @Column(nullable = false)
    private String status; // 'IN_PROGRESS', 'REACHED', 'DELAYED'

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Goal() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public BigDecimal getSavedAmount() { return savedAmount; }
    public void setSavedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String goalName;
        private BigDecimal targetAmount;
        private BigDecimal savedAmount;
        private LocalDate targetDate;
        private String priority;
        private String status;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder goalName(String goalName) { this.goalName = goalName; return this; }
        public Builder targetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; return this; }
        public Builder savedAmount(BigDecimal savedAmount) { this.savedAmount = savedAmount; return this; }
        public Builder targetDate(LocalDate targetDate) { this.targetDate = targetDate; return this; }
        public Builder priority(String priority) { this.priority = priority; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public Goal build() {
            Goal goal = new Goal();
            goal.setId(this.id);
            goal.setUserId(this.userId);
            goal.setGoalName(this.goalName);
            goal.setTargetAmount(this.targetAmount);
            goal.setSavedAmount(this.savedAmount);
            goal.setTargetDate(this.targetDate);
            goal.setPriority(this.priority);
            goal.setStatus(this.status);
            return goal;
        }
    }
}
