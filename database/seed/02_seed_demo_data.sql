-- ============================================================================
-- FINSAFE DEMO SEED DATA
-- Purpose: Realistic financial profiles for demonstration and testing.
-- Provides three distinct user financial personas:
--   1. Aarav Sharma: Financially Healthy / Safe spender (High surplus, Low debt)
--   2. Priya Patel: Borderline / Over-leveraged (High EMI obligations)
--   3. Rahul Verma: Deficit / High Risk (Low liquidity, negative monthly surplus)
-- ============================================================================

-- Clean existing demo records
DELETE FROM users WHERE email IN (
    'aarav.sharma@example.com',
    'priya.patel@example.com',
    'rahul.verma@example.com'
);

-- ============================================================================
-- PERSONA 1: AARAV SHARMA (Healthy / Strong Liquidity)
-- ============================================================================
INSERT INTO users (id, email, password_hash, full_name, age, occupation, employment_status)
VALUES (
    1,
    'aarav.sharma@example.com',
    -- BCrypt hash for "Password@123"
    '$2a$10$7R6vHjG2bW88DkYJvO0Lme5YjB6O0zM9F1XvHn7C3sZ5wK1y6F0m2',
    'Aarav Sharma',
    28,
    'Senior Software Engineer',
    'FULL_TIME'
);

INSERT INTO accounts (user_id, account_name, current_balance, currency)
VALUES (1, 'HDFC Salary Account', 250000.00, 'INR');

INSERT INTO incomes (user_id, source_name, amount, frequency)
VALUES 
    (1, 'Tech Corp Salary', 120000.00, 'MONTHLY'),
    (1, 'Freelance Consulting', 15000.00, 'MONTHLY');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
VALUES 
    (1, 'Rent', 30000.00, 'Apartment rent', CURRENT_DATE - INTERVAL '5 days'),
    (1, 'Groceries', 8000.00, 'Monthly supermarket supplies', CURRENT_DATE - INTERVAL '10 days'),
    (1, 'Utilities', 3500.00, 'Electricity & broadband', CURRENT_DATE - INTERVAL '12 days'),
    (1, 'Dining Out', 5000.00, 'Weekend dining', CURRENT_DATE - INTERVAL '2 days');

INSERT INTO recurring_bills (user_id, biller_name, amount, due_day_of_month)
VALUES 
    (1, 'Apartment Rent', 30000.00, 5),
    (1, 'Airtel Broadband', 1200.00, 15),
    (1, 'Cult.fit Gym', 1800.00, 1);

INSERT INTO emis (user_id, loan_name, principal_amount, monthly_installment, interest_rate_annual, total_tenure_months, remaining_months)
VALUES 
    (1, 'MacBook Pro EMI', 120000.00, 10000.00, 0.00, 12, 3);

INSERT INTO goals (user_id, goal_name, target_amount, saved_amount, target_date, priority)
VALUES 
    (1, 'Emergency Fund (6 Months)', 300000.00, 220000.00, CURRENT_DATE + INTERVAL '180 days', 'HIGH'),
    (1, 'Japan Vacation', 150000.00, 80000.00, CURRENT_DATE + INTERVAL '270 days', 'MEDIUM');

INSERT INTO decisions (
    user_id, item_name, purchase_amount, payment_method,
    candidate_recommendation, final_recommendation, validator_status,
    balance_before_purchase, projected_balance_typical, projected_balance_bad_case, max_safe_purchase,
    triggered_rules, decision_reasons, explanation_text
)
VALUES (
    1,
    'Ergonomic Work Desk',
    25000.00,
    'UPFRONT',
    'Buy Now',
    'Buy Now',
    'PASS',
    250000.00,
    315000.00,
    265000.00,
    255000.00,
    '[]'::jsonb,
    '["V1_INPUT_SANITY: Passed", "V2_AFFORDABILITY: Purchase fits within safe bounds", "V4_SAFETY_BUFFER: High worst-case cushion maintained", "V5_CASH_ON_HAND: Sufficient liquid cash"]'::jsonb,
    'Recommendation: BUY NOW. After paying ₹25,000 upfront, your projected balance in a bad-case month remains a healthy ₹2,65,000, comfortably exceeding your ₹10,000 minimum safety buffer without impacting your Emergency Fund goal.'
);


-- ============================================================================
-- PERSONA 2: PRIYA PATEL (Stretched / High Debt Ratio)
-- ============================================================================
INSERT INTO users (id, email, password_hash, full_name, age, occupation, employment_status)
VALUES (
    2,
    'priya.patel@example.com',
    '$2a$10$7R6vHjG2bW88DkYJvO0Lme5YjB6O0zM9F1XvHn7C3sZ5wK1y6F0m2',
    'Priya Patel',
    26,
    'Marketing Specialist',
    'FULL_TIME'
);

INSERT INTO accounts (user_id, account_name, current_balance, currency)
VALUES (2, 'ICICI Savings', 42000.00, 'INR');

INSERT INTO incomes (user_id, source_name, amount, frequency)
VALUES (2, 'Agency Salary', 65000.00, 'MONTHLY');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
VALUES 
    (2, 'Rent', 18000.00, 'Co-living space rent', CURRENT_DATE - INTERVAL '4 days'),
    (2, 'Food & Groceries', 9000.00, 'Food deliveries and groceries', CURRENT_DATE - INTERVAL '8 days'),
    (2, 'Shopping', 6000.00, 'Clothing purchase', CURRENT_DATE - INTERVAL '15 days');

INSERT INTO emis (user_id, loan_name, principal_amount, monthly_installment, interest_rate_annual, total_tenure_months, remaining_months)
VALUES 
    (2, 'Two-Wheeler Loan', 95000.00, 4800.00, 11.50, 24, 14),
    (2, 'Education Loan', 350000.00, 14500.00, 9.20, 36, 26);
    -- Total existing EMI = ₹19,300 (~30% of income)

INSERT INTO goals (user_id, goal_name, target_amount, saved_amount, target_date, priority)
VALUES 
    (2, 'Emergency Buffer', 100000.00, 30000.00, CURRENT_DATE + INTERVAL '120 days', 'HIGH');

INSERT INTO decisions (
    user_id, item_name, purchase_amount, payment_method,
    candidate_recommendation, final_recommendation, validator_status,
    balance_before_purchase, projected_balance_typical, projected_balance_bad_case, max_safe_purchase,
    triggered_rules, decision_reasons, explanation_text
)
VALUES (
    2,
    'Flagship Smartphone',
    65000.00,
    'UPFRONT',
    'Buy Now',
    'Wait',
    'MODIFY',
    42000.00,
    45000.00,
    8500.00,
    15000.00,
    '["V4_SAFETY_BUFFER", "V5_CASH_ON_HAND"]'::jsonb,
    '["V5_CASH_ON_HAND: Current balance ₹42,000 is insufficient for ₹65,000 upfront payment.", "V4_SAFETY_BUFFER: Post-purchase bad-case balance falls below the ₹10,000 safety threshold."]'::jsonb,
    'Recommendation: WAIT. Paying ₹65,000 upfront is not possible with your current balance of ₹42,000. Even with an EMI, your existing loan commitments take up 30% of your salary. We recommend saving an additional ₹23,000 before proceeding.'
);


-- ============================================================================
-- PERSONA 3: RAHUL VERMA (High Risk / Monthly Deficit)
-- ============================================================================
INSERT INTO users (id, email, password_hash, full_name, age, occupation, employment_status)
VALUES (
    3,
    'rahul.verma@example.com',
    '$2a$10$7R6vHjG2bW88DkYJvO0Lme5YjB6O0zM9F1XvHn7C3sZ5wK1y6F0m2',
    'Rahul Verma',
    23,
    'Junior Graphic Designer',
    'FREELANCE'
);

INSERT INTO accounts (user_id, account_name, current_balance, currency)
VALUES (3, 'SBI Savings', 9500.00, 'INR');

INSERT INTO incomes (user_id, source_name, amount, frequency)
VALUES (3, 'Freelance Retainers', 38000.00, 'MONTHLY');

INSERT INTO expenses (user_id, category, amount, description, expense_date)
VALUES 
    (3, 'Rent', 15000.00, 'Room rent', CURRENT_DATE - INTERVAL '2 days'),
    (3, 'Food', 10000.00, 'Daily food and dining', CURRENT_DATE - INTERVAL '6 days'),
    (3, 'Subscriptions', 4000.00, 'Software subscriptions', CURRENT_DATE - INTERVAL '11 days'),
    (3, 'Miscellaneous', 5000.00, 'General spend', CURRENT_DATE - INTERVAL '14 days');

INSERT INTO emis (user_id, loan_name, principal_amount, monthly_installment, interest_rate_annual, total_tenure_months, remaining_months)
VALUES 
    (3, 'Consumer Durable Loan', 40000.00, 6000.00, 14.00, 8, 5);
    -- Total spend = 15k + 10k + 4k + 5k + 6k = ₹40,000 (Deficit against ₹38,000 income!)

INSERT INTO goals (user_id, goal_name, target_amount, saved_amount, target_date, priority)
VALUES 
    (3, 'Emergency Starter Fund', 50000.00, 5000.00, CURRENT_DATE + INTERVAL '90 days', 'HIGH');

INSERT INTO decisions (
    user_id, item_name, purchase_amount, payment_method,
    candidate_recommendation, final_recommendation, validator_status,
    balance_before_purchase, projected_balance_typical, projected_balance_bad_case, max_safe_purchase,
    triggered_rules, decision_reasons, explanation_text
)
VALUES (
    3,
    'Gaming Console',
    42000.00,
    'UPFRONT',
    'Not Recommended',
    'Not Recommended',
    'PASS',
    9500.00,
    2500.00,
    -12000.00,
    0.00,
    '["V2_AFFORDABILITY", "V4_SAFETY_BUFFER", "V5_CASH_ON_HAND"]'::jsonb,
    '["V2_AFFORDABILITY: Monthly expenses exceed income (monthly deficit of ₹2,000).", "V4_SAFETY_BUFFER: Projected balance is negative.", "V5_CASH_ON_HAND: Current balance of ₹9,500 cannot cover ₹42,000."]'::jsonb,
    'Recommendation: NOT RECOMMENDED. Your monthly expenses and EMI currently exceed your monthly income, resulting in a deficit. Any additional purchase or loan will push your account into a negative balance.'
);

-- Reset sequence counters to avoid duplicate keys on fresh inserts
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('accounts_id_seq', (SELECT MAX(id) FROM accounts));
SELECT setval('incomes_id_seq', (SELECT MAX(id) FROM incomes));
SELECT setval('expenses_id_seq', (SELECT MAX(id) FROM expenses));
SELECT setval('recurring_bills_id_seq', (SELECT MAX(id) FROM recurring_bills));
SELECT setval('emis_id_seq', (SELECT MAX(id) FROM emis));
SELECT setval('goals_id_seq', (SELECT MAX(id) FROM goals));
SELECT setval('decisions_id_seq', (SELECT MAX(id) FROM decisions));
