package com.finsafe.service;

import com.finsafe.dto.DashboardResponse;
import com.finsafe.entity.*;
import com.finsafe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final EmiRepository emiRepository;
    private final GoalRepository goalRepository;
    private final DecisionRepository decisionRepository;

    public DashboardResponse getDashboardData(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUserId(userId)
                .orElse(Account.builder().currentBalance(BigDecimal.ZERO).build());

        List<Income> incomes = incomeRepository.findByUserIdAndIsActiveTrue(userId);
        BigDecimal totalIncome = incomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Recent 30 days expenses
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateAfterOrderByExpenseDateDesc(
                userId, LocalDate.now().minusDays(30));
        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Emi> emis = emiRepository.findByUserIdAndIsActiveTrue(userId);
        BigDecimal totalEmi = emis.stream()
                .map(Emi::getMonthlyInstallment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal surplus = totalIncome.subtract(totalExpenses).subtract(totalEmi);

        List<Goal> goals = goalRepository.findByUserIdOrderByPriorityAsc(userId);
        List<Decision> decisions = decisionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return DashboardResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .currentBalance(account.getCurrentBalance())
                .monthlyIncome(totalIncome)
                .monthlyExpenses(totalExpenses)
                .monthlyEmi(totalEmi)
                .monthlySurplus(surplus)
                .goals(goals.stream().map(g -> DashboardResponse.GoalSummary.builder()
                        .id(g.getId())
                        .name(g.getGoalName())
                        .targetAmount(g.getTargetAmount())
                        .savedAmount(g.getSavedAmount())
                        .priority(g.getPriority())
                        .targetDate(g.getTargetDate().toString())
                        .build()).collect(Collectors.toList()))
                .emis(emis.stream().map(e -> DashboardResponse.EmiSummary.builder()
                        .id(e.getId())
                        .name(e.getLoanName())
                        .monthlyAmount(e.getMonthlyInstallment())
                        .remainingMonths(e.getRemainingMonths())
                        .build()).collect(Collectors.toList()))
                .recentDecisions(decisions.stream().limit(5).map(d -> DashboardResponse.RecentDecisionSummary.builder()
                        .id(d.getId())
                        .item(d.getItemName())
                        .price(d.getPurchaseAmount())
                        .recommendation(d.getFinalRecommendation())
                        .validatorStatus(d.getValidatorStatus())
                        .createdAt(d.getCreatedAt() != null ? d.getCreatedAt().toString() : "")
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
