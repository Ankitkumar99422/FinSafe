# FinSafe — AI Financial Decision Engine

[![Branch](https://img.shields.io/badge/active_branch-test-green.svg)](https://github.com/Ankitkumar99422/FinSafe/tree/test)
[![Architecture](https://img.shields.io/badge/Architecture-Hybrid%20Java%20%2B%20Python%20%2B%20Next.js-blue.svg)](#system-architecture)
[![Database](https://img.shields.io/badge/Database-PostgreSQL%20(Supabase)-3ECF8E.svg)](#technology-stack)

> **FinSafe** is an AI-powered personal finance decision-support system. It evaluates whether a planned purchase is financially sustainable *before* you commit to it, combining financial profiling, multi-scenario cash-flow forecasting, AI candidate generation, and a deterministic rule-based safety engine.

---

## 📌 Problem Statement & Core Promise

Most people evaluate purchase affordability looking only at their current bank balance. This overlooks upcoming bills, existing EMIs, emergency reserves, and protected savings goals — turning seemingly safe purchases into financial strain.

**Core Promise:** FinSafe helps users make informed purchasing decisions by delivering transparent verdicts (**Buy Now**, **EMI**, **Wait**, or **Not Recommended**) backed by mathematical safety buffers and plain-language explanations.

---

## ✨ Key Features

- **📊 Comprehensive Financial Profile**: Manage income streams, categorized expenses, recurring bills/subscriptions, and active EMI obligations.
- **🎯 Goal Protection**: Set priority savings goals (Emergency Fund, Down Payment, etc.) that the decision engine actively protects against being delayed.
- **🔍 Purchase Analyzer**: Evaluate planned purchases (upfront cash vs. proposed EMI tenure and interest).
- **📈 Multi-Scenario Forecasting**: Predicts cash-flow trajectories across typical and adverse/bad-case scenarios.
- **🛡️ Deterministic Safety Validator (V1–V9)**: A rule-based guardrail that strictly validates and overrides AI model suggestions to prevent unsafe recommendations.
- **💬 Natural-Language Explanations**: Generates clear, actionable reasoning via Claude API strictly based on validated financial facts.
- **📜 Decision History & Audit Trail**: Full log of past purchase analyses, verdicts, and triggered safety checks.

---

## 🏗️ System Architecture & Request Flow

FinSafe uses a decoupled, hybrid service architecture:

```
[ Next.js Frontend ]
        │
        ▼ (REST / JWT)
[ Java Spring Boot Backend ] ◄──► [ Supabase (PostgreSQL) ]
        │
        ▼ (Internal REST: /internal/v1/analyze)
[ Python FastAPI Service ]
  ├── 1. Financial Math & Forecasting (Pandas / NumPy)
  ├── 2. Machine Learning Decision Model (Candidate verdict)
  └── 3. Deterministic Safety Validator (V1–V9 Checks)
        │
        ▼ (Validated Decision & Approved Facts)
[ Claude API (Explanation Layer) ]
        │
        ▼
[ Spring Boot saves Decision to Database & responds to Frontend ]
```

### End-to-End Decision Flow:
1. **User Request**: User submits a purchase item, price, and payment method via the Next.js UI.
2. **Context Assembly**: Spring Boot authenticates the user, retrieves financial records (balance, income, active EMIs, goals) from Supabase.
3. **Internal Analysis Call**: Spring Boot dispatches a versioned payload to the Python service (`/internal/v1/analyze`).
4. **Calculations & ML Inference**: Python computes cash-flow projections and feeds features into the trained ML model for a candidate recommendation.
5. **Deterministic Safety Validation**: The candidate verdict passes through the **Safety Validator (V1–V9)**. Precedence: `REJECT` > `MODIFY` > `PASS`.
6. **Explanation**: The validated verdict and supporting facts are passed to the explanation layer (Claude API).
7. **Persistence & Response**: Spring Boot stores the decision and audit trail in PostgreSQL and returns the result to the user.

---

## 🛠️ Technology Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Frontend** | **Next.js 15 (React 19, TypeScript)** | Typed UI, SSR, dynamic routing |
| **Styling & UI** | **Tailwind CSS + shadcn/ui** | Responsive, accessible FinTech design system |
| **Data Visualization** | **Recharts** | Interactive spending trends, cash-flow trajectories, goal progress |
| **Form Handling** | **React Hook Form + Zod** | Robust client-side validation |
| **Core Backend** | **Java 17/21 + Spring Boot 3** | REST APIs, Spring Security (JWT), business orchestration |
| **Data Access (Java)** | **Spring Data JPA / Hibernate** | Type-safe entity persistence and relational mapping |
| **Database** | **Supabase (PostgreSQL)** | ACID compliance, `NUMERIC` precision for currency, `JSONB` audit logs |
| **AI / Finance Service** | **Python 3.11+ + FastAPI + Pydantic** | High-performance async microservice with typed contracts |
| **Financial Engine** | **Pandas + NumPy** | Financial metric aggregations and scenario forecasting |
| **AI / ML Model** | **Trained Classification Model** | Candidate purchase recommendation engine |
| **Explanation Layer** | **Claude API** | Plain-language synthesis of validated financial decisions |
| **Version Control** | **Git + GitHub** | Feature branch collaboration |

---

## 👥 Team Division & Responsibilities

| Role | Primary Owner | Scope & Deliverables |
| :--- | :--- | :--- |
| **Backend Lead** | **Backend Lead (You)** | Spring Boot REST endpoints, JWT authentication, orchestration, database integration via JPA, Python internal HTTP client. |
| **AI / ML Engineer** | **Yashika** | Machine learning model training, candidate recommendation engine, feature preprocessing, model inference module. |
| **Database Specialist** | **Teammate 3** | Supabase/PostgreSQL schema, migrations, relational integrity, indexing, synthetic seed data. |
| **Python Finance & Safety Validator** | **Teammate 4** | FastAPI microservice (`/internal/v1/analyze`), cash-flow forecasting logic, deterministic V1–V9 Safety Validator, Claude API integration. |
| **Frontend Lead** | **Teammate 5** | Next.js UI, responsive dashboard, purchase analyzer form, charts (Recharts), and API integration. |

---

## 🛡️ Safety Validator Rule Catalogue (V1–V9)

The **Safety Validator** ensures AI recommendations never compromise financial stability:

* **Status Precedence**: `REJECT` > `MODIFY` > `PASS` (A malformed or unsafe request is never approved).

| Rule | Check | Action / Outcome |
| :--- | :--- | :--- |
| **V1 — Input Sanity** | Validates data types, required fields, positive price, valid labels | `REJECT` if malformed or incomplete |
| **V2 — Affordability** | Compares purchase amount against computed maximum safe purchase | `MODIFY` to `Wait` if unaffordable |
| **V3 — Forecast Uncertainty** | Checks uncertainty markers in projected cash-flow | `MODIFY` to `Wait` if forecast is volatile |
| **V4 — Safety Buffer** | Checks post-purchase worst-case balance against minimum safety threshold | `MODIFY` to `Wait` if buffer breached |
| **V5 — Cash on Hand** | Verifies current liquid balance covers upfront payment | `MODIFY` to `Wait` or suggest EMI if cash is insufficient |
| **V6 — New EMI Burden** | Compares proposed monthly installment against monthly income | `MODIFY` to `Wait` if ratio exceeds threshold |
| **V7 — Existing Debt Burden** | Evaluates total debt (existing EMIs + new EMI) vs. monthly income | `MODIFY` to `Wait` if cumulative EMI ratio is too high |
| **V8 — Goal Protection** | Detects potential delays to high-priority savings goals | Warn or `MODIFY` based on goal priority |
| **V9 — Audit Log** | Records all triggered rules, timestamps, and candidate-to-final diffs | Audit persistence; does not alter verdict |

---

## 📂 Repository Structure

```
finsafe/
├── frontend/                     # Next.js 15 Web Application
│   ├── app/                      # App router (public, auth, dashboard)
│   ├── components/               # Reusable UI primitives (shadcn/ui)
│   ├── features/                 # Domain-specific UI (analyzer, charts, goals)
│   └── lib/                      # API client and utility functions
│
├── backend-java/                 # Java Spring Boot Core Backend
│   ├── src/main/java/com/finsafe/
│   │   ├── config/               # Security, CORS, WebClient configs
│   │   ├── controller/           # REST API endpoints
│   │   ├── dto/                  # Request/Response data transfer objects
│   │   ├── entity/               # JPA Entities mapped to Supabase
│   │   ├── repository/           # Spring Data JPA repositories
│   │   ├── service/              # Business logic & Python client orchestration
│   │   └── client/               # HTTP client calling Python service
│   └── pom.xml
│
├── ai-service-python/            # FastAPI AI & Financial Engine
│   ├── app/
│   │   ├── main.py               # FastAPI entrypoint
│   │   ├── api/                  # Internal endpoints (/internal/v1/analyze)
│   │   ├── schemas/              # Pydantic request/response schemas
│   │   ├── model/                # ML inference adapter (Yashika)
│   │   ├── finance/              # Cash-flow calculations & forecasting
│   │   ├── validator/            # Deterministic V1–V9 Safety Validator
│   │   └── explanation/          # Claude API explanation generator
│   └── requirements.txt
│
├── database/                     # Database Assets
│   ├── schema/                   # PostgreSQL DDL table definitions
│   ├── migrations/               # Versioned migration scripts
│   └── seed/                     # Demo users, accounts, and financial scenarios
│
└── docs/                         # Project Documentation
    ├── architecture/             # High-level architecture & sequence diagrams
    └── api-contracts/            # Versioned JSON contracts between services
```

---

## 🌿 Git & Branching Strategy

> [!IMPORTANT]
> **Strict Branch Policy**: All team development and pushes must target the **`test`** branch or dedicated feature branches. **Do not push directly to `main`**.

### Branch Workflow:
1. Branch off `test` for any new feature or module:
   ```bash
   git checkout test
   git pull origin test
   git checkout -b feature/backend-auth-jwt
   ```
2. Commit and push your changes to your feature branch:
   ```bash
   git push origin feature/backend-auth-jwt
   ```
3. Open a Pull Request targeting **`test`**.
4. Once reviewed and verified, merge into **`test`**.
