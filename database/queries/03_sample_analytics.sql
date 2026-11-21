-- ============================================================================
-- FINSAFE SAMPLE ANALYTICAL & AUDIT QUERIES
-- Purpose: Ready-to-run SQL queries for demonstrations, evaluations, and tests.
-- Shows how the database supports the Dashboard, Safety Validator, and Decision History.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- QUERY 1: Complete User Financial Health Snapshot (For Dashboard)
-- Calculates: Current Balance, Total Monthly Income, Monthly Expenses, 
--             Active EMI obligations, and Net Monthly Surplus/Deficit.
-- ----------------------------------------------------------------------------
SELECT 
    u.id AS user_id,
    u.full_name,
    a.current_balance,
    COALESCE(i.total_monthly_income, 0.00) AS total_monthly_income,
    COALESCE(e.total_monthly_expenses, 0.00) AS total_monthly_expenses,
    COALESCE(em.total_monthly_emi, 0.00) AS total_monthly_emi,
    (COALESCE(i.total_monthly_income, 0.00) - 
     COALESCE(e.total_monthly_expenses, 0.00) - 
     COALESCE(em.total_monthly_emi, 0.00)) AS net_monthly_surplus
FROM users u
LEFT JOIN accounts a ON a.user_id = u.id
LEFT JOIN (
    SELECT user_id, SUM(amount) AS total_monthly_income
    FROM incomes
    WHERE is_active = TRUE
    GROUP BY user_id
) i ON i.user_id = u.id
LEFT JOIN (
    SELECT user_id, SUM(amount) AS total_monthly_expenses
    FROM expenses
    WHERE expense_date >= CURRENT_DATE - INTERVAL '30 days'
    GROUP BY user_id
) e ON e.user_id = u.id
LEFT JOIN (
    SELECT user_id, SUM(monthly_installment) AS total_monthly_emi
    FROM emis
    WHERE is_active = TRUE
    GROUP BY user_id
) em ON em.user_id = u.id
ORDER BY u.id;


-- ----------------------------------------------------------------------------
-- QUERY 2: Debt-to-Income Ratio (For Rule V7 — Total EMI Burden Check)
-- Validates if a user's total active EMIs exceed the agreed debt ceiling (e.g. 40%).
-- ----------------------------------------------------------------------------
SELECT 
    u.id AS user_id,
    u.full_name,
    COALESCE(i.monthly_income, 0.00) AS monthly_income,
    COALESCE(em.active_emi_total, 0.00) AS total_monthly_emi,
    ROUND(
        (COALESCE(em.active_emi_total, 0.00) / NULLIF(COALESCE(i.monthly_income, 0.00), 0) * 100), 
        2
    ) AS debt_to_income_percentage,
    CASE 
        WHEN (COALESCE(em.active_emi_total, 0.00) / NULLIF(COALESCE(i.monthly_income, 0.00), 0)) > 0.40 
        THEN 'CRITICAL_DEBT_BURDEN'
        WHEN (COALESCE(em.active_emi_total, 0.00) / NULLIF(COALESCE(i.monthly_income, 0.00), 0)) > 0.25 
        THEN 'MODERATE_BURDEN'
        ELSE 'HEALTHY_DEBT_RATIO'
    END AS v7_safety_verdict
FROM users u
LEFT JOIN (
    SELECT user_id, SUM(amount) AS monthly_income
    FROM incomes
    WHERE is_active = TRUE
    GROUP BY user_id
) i ON i.user_id = u.id
LEFT JOIN (
    SELECT user_id, SUM(monthly_installment) AS active_emi_total
    FROM emis
    WHERE is_active = TRUE
    GROUP BY user_id
) em ON em.user_id = u.id;


-- ----------------------------------------------------------------------------
-- QUERY 3: Goal Protection & Progress Tracking (For Rule V8)
-- Displays savings goals, priority weighting, and shortfall to completion.
-- ----------------------------------------------------------------------------
SELECT 
    g.user_id,
    u.full_name,
    g.goal_name,
    g.priority,
    g.target_amount,
    g.saved_amount,
    (g.target_amount - g.saved_amount) AS remaining_shortfall,
    ROUND((g.saved_amount / g.target_amount * 100), 1) AS progress_percentage,
    g.target_date,
    (g.target_date - CURRENT_DATE) AS days_remaining
FROM goals g
JOIN users u ON u.id = g.user_id
WHERE g.status = 'IN_PROGRESS'
ORDER BY g.user_id, CASE g.priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 ELSE 3 END;


-- ----------------------------------------------------------------------------
-- QUERY 4: Purchase Decisions Audit Trail (For Rule V9 & History Page)
-- Demonstrates how Safety Validator modifications are stored with reasons.
-- ----------------------------------------------------------------------------
SELECT 
    d.id AS decision_id,
    u.full_name AS user_name,
    d.item_name,
    d.purchase_amount,
    d.candidate_recommendation AS ai_candidate,
    d.final_recommendation AS final_verdict,
    d.validator_status,
    d.triggered_rules,
    d.decision_reasons,
    d.created_at
FROM decisions d
JOIN users u ON u.id = d.user_id
ORDER BY d.created_at DESC;


-- ----------------------------------------------------------------------------
-- QUERY 5: Spending Breakdown by Category (For Dashboard Recharts/Donut)
-- Aggregates user expenses over the last 30 days for visual breakdown.
-- ----------------------------------------------------------------------------
SELECT 
    category,
    SUM(amount) AS total_spent,
    COUNT(*) AS transaction_count,
    ROUND((SUM(amount) * 100.0 / (SELECT SUM(amount) FROM expenses WHERE user_id = 1)), 2) AS category_share_percent
FROM expenses
WHERE user_id = 1
GROUP BY category
ORDER BY total_spent DESC;
