package com.finsafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "emis")
public class Emi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    @Column(name = "monthly_installment", nullable = false)
    private BigDecimal monthlyInstallment;

    @Column(name = "interest_rate_annual")
    private BigDecimal interestRateAnnual;

    @Column(name = "total_tenure_months", nullable = false)
    private Integer totalTenureMonths;

    @Column(name = "remaining_months", nullable = false)
    private Integer remainingMonths;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Emi() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLoanName() { return loanName; }
    public void setLoanName(String loanName) { this.loanName = loanName; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getMonthlyInstallment() { return monthlyInstallment; }
    public void setMonthlyInstallment(BigDecimal monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; }

    public BigDecimal getInterestRateAnnual() { return interestRateAnnual; }
    public void setInterestRateAnnual(BigDecimal interestRateAnnual) { this.interestRateAnnual = interestRateAnnual; }

    public Integer getTotalTenureMonths() { return totalTenureMonths; }
    public void setTotalTenureMonths(Integer totalTenureMonths) { this.totalTenureMonths = totalTenureMonths; }

    public Integer getRemainingMonths() { return remainingMonths; }
    public void setRemainingMonths(Integer remainingMonths) { this.remainingMonths = remainingMonths; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String loanName;
        private BigDecimal principalAmount;
        private BigDecimal monthlyInstallment;
        private BigDecimal interestRateAnnual;
        private Integer totalTenureMonths;
        private Integer remainingMonths;
        private Boolean isActive;
        private LocalDate startDate;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder loanName(String loanName) { this.loanName = loanName; return this; }
        public Builder principalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; return this; }
        public Builder monthlyInstallment(BigDecimal monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; return this; }
        public Builder interestRateAnnual(BigDecimal interestRateAnnual) { this.interestRateAnnual = interestRateAnnual; return this; }
        public Builder totalTenureMonths(Integer totalTenureMonths) { this.totalTenureMonths = totalTenureMonths; return this; }
        public Builder remainingMonths(Integer remainingMonths) { this.remainingMonths = remainingMonths; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }

        public Emi build() {
            Emi emi = new Emi();
            emi.setId(this.id);
            emi.setUserId(this.userId);
            emi.setLoanName(this.loanName);
            emi.setPrincipalAmount(this.principalAmount);
            emi.setMonthlyInstallment(this.monthlyInstallment);
            emi.setInterestRateAnnual(this.interestRateAnnual);
            emi.setTotalTenureMonths(this.totalTenureMonths);
            emi.setRemainingMonths(this.remainingMonths);
            emi.setIsActive(this.isActive);
            emi.setStartDate(this.startDate);
            return emi;
        }
    }
}
