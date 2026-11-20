-- ============================================================================
-- FINSAFE DATABASE SCHEMA (PostgreSQL / Supabase)
-- Project: FinSafe — AI Financial Decision Engine
-- Module: Database Architecture & Data Integrity
-- ============================================================================

-- Enable UUID extension if needed in future
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Drop tables in reverse dependency order for clean reinstalls
DROP TABLE IF EXISTS decisions CASCADE;
DROP TABLE IF EXISTS goals CASCADE;
DROP TABLE IF EXISTS emis CASCADE;
DROP TABLE IF EXISTS recurring_bills CASCADE;
DROP TABLE IF EXISTS expenses CASCADE;
DROP TABLE IF EXISTS incomes CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================================
-- 1. USERS TABLE
-- Manages user identity, secure authentication, and profile metadata
-- ============================================================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    age INT CHECK (age IS NULL OR (age >= 18 AND age <= 120)),
    occupation VARCHAR(100),
    employment_status VARCHAR(50), -- 'FULL_TIME', 'PART_TIME', 'FREELANCE', 'STUDENT', 'BUSINESS'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- ============================================================================
-- 2. ACCOUNTS TABLE
-- Holds the liquid bank balance and currency for safety checks (Rule V5)
-- ============================================================================
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_name VARCHAR(100) DEFAULT 'Primary Savings',
    current_balance NUMERIC(14, 2) NOT NULL DEFAULT 0.00 CHECK (current_balance >= 0),
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    last_synced_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_account UNIQUE (user_id, account_name)
);

CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- ============================================================================
-- 3. INCOMES TABLE
-- Records steady and secondary income streams for monthly budget baselines
-- ============================================================================
CREATE TABLE incomes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    source_name VARCHAR(150) NOT NULL, -- e.g., 'Primary Salary', 'Freelance Consulting'
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    frequency VARCHAR(50) NOT NULL DEFAULT 'MONTHLY', -- 'MONTHLY', 'BI_WEEKLY', 'ANNUAL'
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_incomes_user_id ON incomes(user_id);

-- ============================================================================
-- 4. EXPENSES TABLE
-- Tracks historical and monthly outlays grouped by spending category
-- ============================================================================
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL, -- 'Rent', 'Groceries', 'Utilities', 'Entertainment', 'Healthcare', 'Other'
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    description TEXT,
    expense_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_expenses_user_date ON expenses(user_id, expense_date);
CREATE INDEX idx_expenses_category ON expenses(user_id, category);

-- ============================================================================
-- 5. RECURRING BILLS TABLE
-- Tracks fixed monthly commitments (Rent, Utilities, Subscriptions)
-- ============================================================================
CREATE TABLE recurring_bills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    biller_name VARCHAR(150) NOT NULL, -- e.g., 'House Rent', 'Electricity', 'Netflix'
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    due_day_of_month INT NOT NULL CHECK (due_day_of_month BETWEEN 1 AND 31),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recurring_bills_user ON recurring_bills(user_id);

-- ============================================================================
-- 6. EMIS / LOANS TABLE
-- Active debt obligations. Critical for Debt Burden verification (Rules V6 & V7)
-- ============================================================================
CREATE TABLE emis (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    loan_name VARCHAR(150) NOT NULL, -- e.g., 'Vehicle Loan', 'Smartphone EMI'
    principal_amount NUMERIC(14, 2) NOT NULL CHECK (principal_amount > 0),
    monthly_installment NUMERIC(12, 2) NOT NULL CHECK (monthly_installment > 0),
    interest_rate_annual NUMERIC(5, 2) DEFAULT 0.00 CHECK (interest_rate_annual >= 0),
    total_tenure_months INT NOT NULL CHECK (total_tenure_months > 0),
    remaining_months INT NOT NULL CHECK (remaining_months >= 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    start_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_emis_user_active ON emis(user_id, is_active);

-- ============================================================================
-- 7. SAVINGS GOALS TABLE
-- Savings goals protected against delays by proposed purchases (Rule V8)
-- ============================================================================
CREATE TABLE goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    goal_name VARCHAR(150) NOT NULL, -- e.g., 'Emergency Fund', 'Laptop Purchase', 'Home Down Payment'
    target_amount NUMERIC(14, 2) NOT NULL CHECK (target_amount > 0),
    saved_amount NUMERIC(14, 2) NOT NULL DEFAULT 0.00 CHECK (saved_amount >= 0),
    target_date DATE NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'HIGH', -- 'HIGH', 'MEDIUM', 'LOW'
    status VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS', -- 'IN_PROGRESS', 'REACHED', 'DELAYED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_goals_user ON goals(user_id, priority);

-- ============================================================================
-- 8. DECISIONS & AUDIT LOG TABLE
-- Stores full purchase requests, candidate outputs, and final validated decisions (Rule V9)
-- ============================================================================
CREATE TABLE decisions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item_name VARCHAR(255) NOT NULL,
    purchase_amount NUMERIC(14, 2) NOT NULL CHECK (purchase_amount > 0),
    payment_method VARCHAR(50) NOT NULL, -- 'UPFRONT' or 'EMI'
    
    -- Recommendation outcomes
    candidate_recommendation VARCHAR(50) NOT NULL, -- Model's initial verdict ('Buy Now', 'EMI', 'Wait', 'Not Recommended')
    final_recommendation VARCHAR(50) NOT NULL,     -- Final verdict after Safety Validator
    validator_status VARCHAR(20) NOT NULL,         -- 'PASS', 'MODIFY', 'REJECT'
    
    -- Numerical snapshot at decision time
    balance_before_purchase NUMERIC(14, 2) NOT NULL,
    projected_balance_typical NUMERIC(14, 2),
    projected_balance_bad_case NUMERIC(14, 2),
    max_safe_purchase NUMERIC(14, 2),
    
    -- Detailed rule and audit payload
    triggered_rules JSONB,    -- Array of triggered rules (e.g. ['V4_SAFETY_BUFFER', 'V7_EXISTING_EMI'])
    decision_reasons JSONB,   -- Detailed explanation strings for each rule
    explanation_text TEXT,    -- Human-readable synthesis produced by Claude API
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_decisions_user_created ON decisions(user_id, created_at DESC);
CREATE INDEX idx_decisions_verdict ON decisions(final_recommendation);
