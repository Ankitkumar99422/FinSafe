package com.finsafe.repository;

import com.finsafe.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdAndExpenseDateAfterOrderByExpenseDateDesc(Long userId, LocalDate date);
    List<Expense> findByUserIdOrderByExpenseDateDesc(Long userId);
}
