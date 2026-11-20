package com.finsafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "emis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
