# FinSafe Core Java Spring Boot Backend

[![Framework](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/PostgreSQL-Supabase%20(zlpvkkccsfdlhypnmvc)-3ECF8E.svg)](#configuration)
[![Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-red.svg)](#api-endpoints)

Core enterprise backend for the **FinSafe: AI Financial Decision Engine**. It connects the **Next.js Frontend**, **Supabase Cloud Database**, and **Python AI Financial Service**.

---

## ⚙️ Quick Configuration (Your Step)

Before running the backend, provide your Supabase database password:

### **Option A: Edit `application.properties` Directly**
Open [`src/main/resources/application.properties`](file:///c:/Users/Lenovo/FinSafe/backend-java/src/main/resources/application.properties) and update line 14:
```properties
spring.datasource.password=YOUR_ACTUAL_SUPABASE_PASSWORD
```

### **Option B: Set via Environment Variable (Recommended for Security)**
In your PowerShell terminal before running:
```powershell
$env:SUPABASE_DB_PASSWORD="your-actual-supabase-password"
```

---

## 🚀 How to Run the Backend

### **Method 1: Using IntelliJ IDEA / VS Code / Eclipse (Easiest)**
1. Open the project in your IDE.
2. Locate: `src/main/java/com/finsafe/FinSafeApplication.java`
3. Click the green **Run ▶️** icon.

### **Method 2: Using Maven in Terminal**
```bash
cd backend-java
mvn spring-boot:run
```

Once started, the backend will be live on:
👉 **`http://localhost:8080`**

---

## 📡 API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/health` | Backend and Supabase connectivity health check | ❌ Public |
| `POST` | `/api/auth/register` | Register new user and returns JWT token | ❌ Public |
| `POST` | `/api/auth/login` | Login with email and password | ❌ Public |
| `GET` | `/api/dashboard` | Returns balance, income, expenses, EMIs, goals, surplus | ✅ JWT (or demo user 1) |
| `POST` | `/api/purchases/analyze` | Evaluates purchase with V1–V9 Safety Validator & AI | ✅ JWT (or demo user 1) |

---

## 🏗️ Architecture & Modules

* **`com.finsafe.config`**: Spring Security 6, JWT Authentication Filter, CORS rules for Next.js.
* **`com.finsafe.entity`**: 8 JPA entities mapped to Supabase (`User`, `Account`, `Income`, `Expense`, `RecurringBill`, `Emi`, `Goal`, `Decision`).
* **`com.finsafe.repository`**: Spring Data JPA repositories with custom query methods.
* **`com.finsafe.service`**: Business orchestration, surplus calculations, and deterministic V1–V9 Safety Validator execution.
* **`com.finsafe.controller`**: REST controllers serving API payloads to the Next.js frontend.
