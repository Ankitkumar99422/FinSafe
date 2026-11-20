package com.finsafe.service;

import com.finsafe.dto.PurchaseDtos.*;
import com.finsafe.entity.*;
import com.finsafe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final AccountRepository accountRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final EmiRepository emiRepository;
    private final GoalRepository goalRepository;
    private final DecisionRepository decisionRepository;

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalyzeResponse analyzePurchase(Long userId, AnalyzeRequest request) {
        // 1. Load User's current financial context from Supabase
        Account account = accountRepository.findByUserId(userId)
                .orElse(Account.builder().currentBalance(BigDecimal.ZERO).build());
        BigDecimal balance = account.getCurrentBalance();

        List<Income> incomes = incomeRepository.findByUserIdAndIsActiveTrue(userId);
        BigDecimal totalIncome = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateAfterOrderByExpenseDateDesc(
                userId, LocalDate.now().minusDays(30));
        BigDecimal totalExpenses = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Emi> emis = emiRepository.findByUserIdAndIsActiveTrue(userId);
        BigDecimal totalEmi = emis.stream().map(Emi::getMonthlyInstallment).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Goal> goals = goalRepository.findByUserIdOrderByPriorityAsc(userId);

        BigDecimal price = request.getPrice();
        BigDecimal minSafetyBuffer = new BigDecimal("10000.00");

        // 2. Perform Financial Math & Deterministic V1-V9 Verification
        // (Precedence: REJECT > MODIFY > PASS)
        String candidateRec;
        String finalRec;
        String validatorStatus = "PASS";
        List<String> triggeredRules = new ArrayList<>();
        List<String> reasons = new ArrayList<>();

        BigDecimal badCaseExpenses = totalExpenses.multiply(new BigDecimal("1.25")); // 25% adverse stress buffer
        BigDecimal projectedTypical = balance.add(totalIncome).subtract(totalExpenses).subtract(totalEmi);
        BigDecimal projectedBadCase = balance.add(totalIncome).subtract(badCaseExpenses).subtract(totalEmi);
        BigDecimal maxSafePurchase = projectedBadCase.subtract(minSafetyBuffer).max(BigDecimal.ZERO);

        // V1: Input Sanity
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            candidateRec = "Not Recommended";
            finalRec = "Not Recommended";
            validatorStatus = "REJECT";
            triggeredRules.add("V1_INPUT_SANITY");
            reasons.add("Purchase price must be positive.");
        } else {
            // V5: Cash on Hand
            boolean hasCash = balance.compareTo(price) >= 0;

            // V4: Safety Buffer
            BigDecimal afterBadCase = projectedBadCase.subtract(price);
            boolean bufferPreserved = afterBadCase.compareTo(minSafetyBuffer) >= 0;

            // Candidate model verdict
            if (hasCash && bufferPreserved) {
                candidateRec = "Buy Now";
            } else if (totalIncome.compareTo(BigDecimal.ZERO) > 0 &&
                    totalEmi.add(price.divide(new BigDecimal("6"), RoundingMode.HALF_UP))
                            .divide(totalIncome, 2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.40")) <= 0) {
                candidateRec = "EMI";
            } else if (projectedTypical.subtract(price).compareTo(minSafetyBuffer) >= 0) {
                candidateRec = "Wait";
            } else {
                candidateRec = "Not Recommended";
            }

            // Safety Validator Execution
            finalRec = candidateRec;
            if ("Buy Now".equals(candidateRec)) {
                if (!hasCash) {
                    finalRec = "Wait";
                    validatorStatus = "MODIFY";
                    triggeredRules.add("V5_CASH_ON_HAND");
                    reasons.add("Current balance (₹" + balance + ") is insufficient for upfront cash purchase.");
                } else if (!bufferPreserved) {
                    finalRec = "Wait";
                    validatorStatus = "MODIFY";
                    triggeredRules.add("V4_SAFETY_BUFFER");
                    reasons.add("Post-purchase balance in an adverse month falls below your ₹10,000 minimum safety buffer.");
                }
            }

            // V7: Existing Obligations Check
            BigDecimal newEmiEstimate = price.divide(new BigDecimal("6"), RoundingMode.HALF_UP);
            if (totalIncome.compareTo(BigDecimal.ZERO) > 0 &&
                    totalEmi.add(newEmiEstimate).divide(totalIncome, 2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.45")) > 0) {
                if ("EMI".equals(finalRec)) {
                    finalRec = "Wait";
                    validatorStatus = "MODIFY";
                    triggeredRules.add("V7_EXISTING_OBLIGATIONS");
                    reasons.add("New EMI will cause your total debt obligations to exceed 45% of monthly income.");
                }
            }
        }

        // 3. Human-Readable Explanation
        String explanation;
        if ("Buy Now".equals(finalRec)) {
            explanation = "Recommendation: BUY NOW. After paying ₹" + price + ", your projected bad-case balance remains ₹" +
                    projectedBadCase.subtract(price) + ", comfortably exceeding your ₹10,000 safety threshold.";
        } else if ("EMI".equals(finalRec)) {
            explanation = "Recommendation: EMI. Paying upfront stresses your liquidity, but a 6-month EMI of ~₹" +
                    price.divide(new BigDecimal("6"), RoundingMode.HALF_UP) + " fits safely within your monthly income.";
        } else if ("Wait".equals(finalRec)) {
            explanation = "Recommendation: WAIT. " + (reasons.isEmpty() ? "We recommend saving more before purchasing." : String.join(" ", reasons));
        } else {
            explanation = "Recommendation: NOT RECOMMENDED. This purchase exceeds your safe financial capacity and will cause a deficit.";
        }

        // 4. Save decision audit to Supabase
        Decision decision = Decision.builder()
                .userId(userId)
                .itemName(request.getItem())
                .purchaseAmount(price)
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "UPFRONT")
                .candidateRecommendation(candidateRec)
                .finalRecommendation(finalRec)
                .validatorStatus(validatorStatus)
                .balanceBeforePurchase(balance)
                .projectedBalanceTypical(projectedTypical)
                .projectedBalanceBadCase(projectedBadCase)
                .maxSafePurchase(maxSafePurchase)
                .triggeredRules("[\"" + String.join("\", \"", triggeredRules) + "\"]")
                .decisionReasons("[\"" + String.join("\", \"", reasons) + "\"]")
                .explanationText(explanation)
                .build();

        decision = decisionRepository.save(decision);

        return AnalyzeResponse.builder()
                .decisionId(decision.getId())
                .item(request.getItem())
                .price(price)
                .finalRecommendation(finalRec)
                .candidateRecommendation(candidateRec)
                .validatorStatus(validatorStatus)
                .balanceBeforePurchase(balance)
                .projectedBalanceTypical(projectedTypical)
                .projectedBalanceBadCase(projectedBadCase)
                .maxSafePurchase(maxSafePurchase)
                .triggeredRules(triggeredRules)
                .reasons(reasons)
                .explanation(explanation)
                .build();
    }
}
