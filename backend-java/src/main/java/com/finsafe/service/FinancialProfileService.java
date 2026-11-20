package com.finsafe.service;

import com.finsafe.dto.FinancialProfileDtos.*;
import com.finsafe.entity.*;
import com.finsafe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialProfileService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final EmiRepository emiRepository;
    private final GoalRepository goalRepository;

    public FinancialProfileService(UserRepository userRepository,
                                   AccountRepository accountRepository,
                                   IncomeRepository incomeRepository,
                                   ExpenseRepository expenseRepository,
                                   EmiRepository emiRepository,
                                   GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.emiRepository = emiRepository;
        this.goalRepository = goalRepository;
    }

    @Transactional
    public ProfileResponse saveProfile(Long userId, ProfileSaveRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (req.getAge() != null) user.setAge(req.getAge());
        if (req.getOccupation() != null) user.setOccupation(req.getOccupation());
        if (req.getEmploymentStatus() != null) user.setEmploymentStatus(req.getEmploymentStatus());
        userRepository.save(user);

        // Update liquid account balance
        Account account = accountRepository.findByUserId(userId)
                .orElse(Account.builder().userId(userId).accountName("Primary Savings").currency("INR").build());
        if (req.getCurrentBalance() != null) {
            account.setCurrentBalance(req.getCurrentBalance());
        }
        accountRepository.save(account);

        // Save income stream
        if (req.getMonthlyIncome() != null && req.getMonthlyIncome().compareTo(BigDecimal.ZERO) > 0) {
            List<Income> existing = incomeRepository.findByUserIdAndIsActiveTrue(userId);
            if (existing.isEmpty()) {
                Income income = Income.builder()
                        .userId(userId)
                        .sourceName("Primary Income")
                        .amount(req.getMonthlyIncome())
                        .frequency("MONTHLY")
                        .isActive(true)
                        .build();
                incomeRepository.save(income);
            } else {
                existing.get(0).setAmount(req.getMonthlyIncome());
                incomeRepository.save(existing.get(0));
            }
        }

        // Save baseline expense
        if (req.getMonthlyExpenses() != null && req.getMonthlyExpenses().compareTo(BigDecimal.ZERO) > 0) {
            Expense expense = Expense.builder()
                    .userId(userId)
                    .category("Living Expenses")
                    .amount(req.getMonthlyExpenses())
                    .description("Monthly baseline expenses")
                    .expenseDate(LocalDate.now())
                    .build();
            expenseRepository.save(expense);
        }

        // Save active EMI if provided
        if (req.getMonthlyEmi() != null && req.getMonthlyEmi().compareTo(BigDecimal.ZERO) > 0) {
            Emi emi = Emi.builder()
                    .userId(userId)
                    .loanName("Monthly Commitment")
                    .principalAmount(req.getMonthlyEmi().multiply(new BigDecimal("12")))
                    .monthlyInstallment(req.getMonthlyEmi())
                    .interestRateAnnual(BigDecimal.ZERO)
                    .totalTenureMonths(12)
                    .remainingMonths(12)
                    .isActive(true)
                    .startDate(LocalDate.now())
                    .build();
            emiRepository.save(emi);
        }

        // Save goals
        if (req.getGoals() != null) {
            for (GoalItem g : req.getGoals()) {
                Goal goal = Goal.builder()
                        .userId(userId)
                        .goalName(g.getGoalName())
                        .targetAmount(g.getTargetAmount() != null ? g.getTargetAmount() : new BigDecimal("100000"))
                        .savedAmount(g.getSavedAmount() != null ? g.getSavedAmount() : BigDecimal.ZERO)
                        .targetDate(g.getTargetDate() != null ? LocalDate.parse(g.getTargetDate()) : LocalDate.now().plusMonths(12))
                        .priority(g.getPriority() != null ? g.getPriority() : "HIGH")
                        .status("IN_PROGRESS")
                        .build();
                goalRepository.save(goal);
            }
        }

        return getProfile(userId);
    }

    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUserId(userId)
                .orElse(Account.builder().currentBalance(BigDecimal.ZERO).build());

        BigDecimal income = incomeRepository.findByUserIdAndIsActiveTrue(userId).stream()
                .map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenses = expenseRepository.findByUserIdAndExpenseDateAfterOrderByExpenseDateDesc(userId, LocalDate.now().minusDays(30))
                .stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal emi = emiRepository.findByUserIdAndIsActiveTrue(userId).stream()
                .map(Emi::getMonthlyInstallment).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<GoalItem> goalItems = goalRepository.findByUserIdOrderByPriorityAsc(userId).stream()
                .map(g -> GoalItem.builder()
                        .goalName(g.getGoalName())
                        .targetAmount(g.getTargetAmount())
                        .savedAmount(g.getSavedAmount())
                        .targetDate(g.getTargetDate().toString())
                        .priority(g.getPriority())
                        .build())
                .collect(Collectors.toList());

        return ProfileResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .age(user.getAge())
                .occupation(user.getOccupation())
                .employmentStatus(user.getEmploymentStatus())
                .currentBalance(account.getCurrentBalance())
                .monthlyIncome(income)
                .monthlyExpenses(expenses)
                .monthlyEmi(emi)
                .goals(goalItems)
                .build();
    }
}
