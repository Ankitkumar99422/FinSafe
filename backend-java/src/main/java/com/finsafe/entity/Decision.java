package com.finsafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "decisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "purchase_amount", nullable = false)
    private BigDecimal purchaseAmount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "candidate_recommendation", nullable = false)
    private String candidateRecommendation;

    @Column(name = "final_recommendation", nullable = false)
    private String finalRecommendation;

    @Column(name = "validator_status", nullable = false)
    private String validatorStatus;

    @Column(name = "balance_before_purchase", nullable = false)
    private BigDecimal balanceBeforePurchase;

    @Column(name = "projected_balance_typical")
    private BigDecimal projectedBalanceTypical;

    @Column(name = "projected_balance_bad_case")
    private BigDecimal projectedBalanceBadCase;

    @Column(name = "max_safe_purchase")
    private BigDecimal maxSafePurchase;

    @Column(name = "triggered_rules", columnDefinition = "jsonb")
    private String triggeredRules;

    @Column(name = "decision_reasons", columnDefinition = "jsonb")
    private String decisionReasons;

    @Column(name = "explanation_text")
    private String explanationText;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}
