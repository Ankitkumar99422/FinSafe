package com.finsafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "decisions")
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

    public Decision() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public BigDecimal getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(BigDecimal purchaseAmount) { this.purchaseAmount = purchaseAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCandidateRecommendation() { return candidateRecommendation; }
    public void setCandidateRecommendation(String candidateRecommendation) { this.candidateRecommendation = candidateRecommendation; }

    public String getFinalRecommendation() { return finalRecommendation; }
    public void setFinalRecommendation(String finalRecommendation) { this.finalRecommendation = finalRecommendation; }

    public String getValidatorStatus() { return validatorStatus; }
    public void setValidatorStatus(String validatorStatus) { this.validatorStatus = validatorStatus; }

    public BigDecimal getBalanceBeforePurchase() { return balanceBeforePurchase; }
    public void setBalanceBeforePurchase(BigDecimal balanceBeforePurchase) { this.balanceBeforePurchase = balanceBeforePurchase; }

    public BigDecimal getProjectedBalanceTypical() { return projectedBalanceTypical; }
    public void setProjectedBalanceTypical(BigDecimal projectedBalanceTypical) { this.projectedBalanceTypical = projectedBalanceTypical; }

    public BigDecimal getProjectedBalanceBadCase() { return projectedBalanceBadCase; }
    public void setProjectedBalanceBadCase(BigDecimal projectedBalanceBadCase) { this.projectedBalanceBadCase = projectedBalanceBadCase; }

    public BigDecimal getMaxSafePurchase() { return maxSafePurchase; }
    public void setMaxSafePurchase(BigDecimal maxSafePurchase) { this.maxSafePurchase = maxSafePurchase; }

    public String getTriggeredRules() { return triggeredRules; }
    public void setTriggeredRules(String triggeredRules) { this.triggeredRules = triggeredRules; }

    public String getDecisionReasons() { return decisionReasons; }
    public void setDecisionReasons(String decisionReasons) { this.decisionReasons = decisionReasons; }

    public String getExplanationText() { return explanationText; }
    public void setExplanationText(String explanationText) { this.explanationText = explanationText; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String itemName;
        private BigDecimal purchaseAmount;
        private String paymentMethod;
        private String candidateRecommendation;
        private String finalRecommendation;
        private String validatorStatus;
        private BigDecimal balanceBeforePurchase;
        private BigDecimal projectedBalanceTypical;
        private BigDecimal projectedBalanceBadCase;
        private BigDecimal maxSafePurchase;
        private String triggeredRules;
        private String decisionReasons;
        private String explanationText;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder itemName(String itemName) { this.itemName = itemName; return this; }
        public Builder purchaseAmount(BigDecimal purchaseAmount) { this.purchaseAmount = purchaseAmount; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder candidateRecommendation(String candidateRecommendation) { this.candidateRecommendation = candidateRecommendation; return this; }
        public Builder finalRecommendation(String finalRecommendation) { this.finalRecommendation = finalRecommendation; return this; }
        public Builder validatorStatus(String validatorStatus) { this.validatorStatus = validatorStatus; return this; }
        public Builder balanceBeforePurchase(BigDecimal balanceBeforePurchase) { this.balanceBeforePurchase = balanceBeforePurchase; return this; }
        public Builder projectedBalanceTypical(BigDecimal projectedBalanceTypical) { this.projectedBalanceTypical = projectedBalanceTypical; return this; }
        public Builder projectedBalanceBadCase(BigDecimal projectedBalanceBadCase) { this.projectedBalanceBadCase = projectedBalanceBadCase; return this; }
        public Builder maxSafePurchase(BigDecimal maxSafePurchase) { this.maxSafePurchase = maxSafePurchase; return this; }
        public Builder triggeredRules(String triggeredRules) { this.triggeredRules = triggeredRules; return this; }
        public Builder decisionReasons(String decisionReasons) { this.decisionReasons = decisionReasons; return this; }
        public Builder explanationText(String explanationText) { this.explanationText = explanationText; return this; }

        public Decision build() {
            Decision d = new Decision();
            d.setId(this.id);
            d.setUserId(this.userId);
            d.setItemName(this.itemName);
            d.setPurchaseAmount(this.purchaseAmount);
            d.setPaymentMethod(this.paymentMethod);
            d.setCandidateRecommendation(this.candidateRecommendation);
            d.setFinalRecommendation(this.finalRecommendation);
            d.setValidatorStatus(this.validatorStatus);
            d.setBalanceBeforePurchase(this.balanceBeforePurchase);
            d.setProjectedBalanceTypical(this.projectedBalanceTypical);
            d.setProjectedBalanceBadCase(this.projectedBalanceBadCase);
            d.setMaxSafePurchase(this.maxSafePurchase);
            d.setTriggeredRules(this.triggeredRules);
            d.setDecisionReasons(this.decisionReasons);
            d.setExplanationText(this.explanationText);
            return d;
        }
    }
}
