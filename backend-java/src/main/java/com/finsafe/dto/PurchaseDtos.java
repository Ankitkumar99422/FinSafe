package com.finsafe.dto;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseDtos {

    public static class AnalyzeRequest {
        private String item;
        private BigDecimal price;
        private String paymentMethod; // 'UPFRONT' or 'EMI'

        public AnalyzeRequest() {}

        public AnalyzeRequest(String item, BigDecimal price, String paymentMethod) {
            this.item = item;
            this.price = price;
            this.paymentMethod = paymentMethod;
        }

        public String getItem() { return item; }
        public void setItem(String item) { this.item = item; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    public static class AnalyzeResponse {
        private Long decisionId;
        private String item;
        private BigDecimal price;
        private String finalRecommendation; // 'Buy Now', 'EMI', 'Wait', 'Not Recommended'
        private String candidateRecommendation;
        private String validatorStatus; // 'PASS', 'MODIFY', 'REJECT'
        private BigDecimal balanceBeforePurchase;
        private BigDecimal projectedBalanceTypical;
        private BigDecimal projectedBalanceBadCase;
        private BigDecimal maxSafePurchase;
        private List<String> triggeredRules;
        private List<String> reasons;
        private String explanation;

        public AnalyzeResponse() {}

        public Long getDecisionId() { return decisionId; }
        public void setDecisionId(Long decisionId) { this.decisionId = decisionId; }

        public String getItem() { return item; }
        public void setItem(String item) { this.item = item; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public String getFinalRecommendation() { return finalRecommendation; }
        public void setFinalRecommendation(String finalRecommendation) { this.finalRecommendation = finalRecommendation; }

        public String getCandidateRecommendation() { return candidateRecommendation; }
        public void setCandidateRecommendation(String candidateRecommendation) { this.candidateRecommendation = candidateRecommendation; }

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

        public List<String> getTriggeredRules() { return triggeredRules; }
        public void setTriggeredRules(List<String> triggeredRules) { this.triggeredRules = triggeredRules; }

        public List<String> getReasons() { return reasons; }
        public void setReasons(List<String> reasons) { this.reasons = reasons; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long decisionId;
            private String item;
            private BigDecimal price;
            private String finalRecommendation;
            private String candidateRecommendation;
            private String validatorStatus;
            private BigDecimal balanceBeforePurchase;
            private BigDecimal projectedBalanceTypical;
            private BigDecimal projectedBalanceBadCase;
            private BigDecimal maxSafePurchase;
            private List<String> triggeredRules;
            private List<String> reasons;
            private String explanation;

            public Builder decisionId(Long decisionId) { this.decisionId = decisionId; return this; }
            public Builder item(String item) { this.item = item; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }
            public Builder finalRecommendation(String finalRecommendation) { this.finalRecommendation = finalRecommendation; return this; }
            public Builder candidateRecommendation(String candidateRecommendation) { this.candidateRecommendation = candidateRecommendation; return this; }
            public Builder validatorStatus(String validatorStatus) { this.validatorStatus = validatorStatus; return this; }
            public Builder balanceBeforePurchase(BigDecimal balanceBeforePurchase) { this.balanceBeforePurchase = balanceBeforePurchase; return this; }
            public Builder projectedBalanceTypical(BigDecimal projectedBalanceTypical) { this.projectedBalanceTypical = projectedBalanceTypical; return this; }
            public Builder projectedBalanceBadCase(BigDecimal projectedBalanceBadCase) { this.projectedBalanceBadCase = projectedBalanceBadCase; return this; }
            public Builder maxSafePurchase(BigDecimal maxSafePurchase) { this.maxSafePurchase = maxSafePurchase; return this; }
            public Builder triggeredRules(List<String> triggeredRules) { this.triggeredRules = triggeredRules; return this; }
            public Builder reasons(List<String> reasons) { this.reasons = reasons; return this; }
            public Builder explanation(String explanation) { this.explanation = explanation; return this; }

            public AnalyzeResponse build() {
                AnalyzeResponse res = new AnalyzeResponse();
                res.setDecisionId(this.decisionId);
                res.setItem(this.item);
                res.setPrice(this.price);
                res.setFinalRecommendation(this.finalRecommendation);
                res.setCandidateRecommendation(this.candidateRecommendation);
                res.setValidatorStatus(this.validatorStatus);
                res.setBalanceBeforePurchase(this.balanceBeforePurchase);
                res.setProjectedBalanceTypical(this.projectedBalanceTypical);
                res.setProjectedBalanceBadCase(this.projectedBalanceBadCase);
                res.setMaxSafePurchase(this.maxSafePurchase);
                res.setTriggeredRules(this.triggeredRules);
                res.setReasons(this.reasons);
                res.setExplanation(this.explanation);
                return res;
            }
        }
    }
}
