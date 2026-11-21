package com.finsafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "recurring_bills")
public class RecurringBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "biller_name", nullable = false)
    private String billerName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "due_day_of_month", nullable = false)
    private Integer dueDayOfMonth;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public RecurringBill() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getBillerName() { return billerName; }
    public void setBillerName(String billerName) { this.billerName = billerName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getDueDayOfMonth() { return dueDayOfMonth; }
    public void setDueDayOfMonth(Integer dueDayOfMonth) { this.dueDayOfMonth = dueDayOfMonth; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}
