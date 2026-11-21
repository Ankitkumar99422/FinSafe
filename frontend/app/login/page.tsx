"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
  ShieldCheck,
  Eye,
  EyeOff,
  Mail,
  LockKeyhole,
  ArrowRight,
  CheckCircle2,
  Wallet,
  TrendingUp,
  Sparkles,
  Zap,
} from "lucide-react";
import { API_BASE_URL } from "@/lib/api";

// Pre-configured profiles matching Supabase demo seed data
const DEMO_PERSONAS: Record<string, {
  user: { name: string; email: string };
  finances: { monthlyIncome: number; currentBalance: number; monthlyExpenses: number; monthlyEmi: number };
  goals: string[];
  targetAmount: number;
  targetTimeline: string;
}> = {
  "aarav.sharma@example.com": {
    user: { name: "Aarav Sharma", email: "aarav.sharma@example.com" },
    finances: {
      monthlyIncome: 135000,
      currentBalance: 250000,
      monthlyExpenses: 46500,
      monthlyEmi: 10000,
    },
    goals: ["Emergency Fund", "Travel"],
    targetAmount: 300000,
    targetTimeline: "6 Months",
  },
  "priya.patel@example.com": {
    user: { name: "Priya Patel", email: "priya.patel@example.com" },
    finances: {
      monthlyIncome: 65000,
      currentBalance: 42000,
      monthlyExpenses: 33000,
      monthlyEmi: 19300,
    },
    goals: ["Emergency Fund"],
    targetAmount: 100000,
    targetTimeline: "4 Months",
  },
  "rahul.verma@example.com": {
    user: { name: "Rahul Verma", email: "rahul.verma@example.com" },
    finances: {
      monthlyIncome: 38000,
      currentBalance: 9500,
      monthlyExpenses: 34000,
      monthlyEmi: 6000,
    },
    goals: ["Emergency Fund"],
    targetAmount: 50000,
    targetTimeline: "3 Months",
  },
};

export default function LoginPage() {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  function logInWithPersona(personaEmail: string) {
    const persona = DEMO_PERSONAS[personaEmail];
    if (!persona) return;

    localStorage.setItem("finsafe_user", JSON.stringify(persona.user));
    localStorage.setItem(
      "finsafe_onboarding",
      JSON.stringify({
        user: persona.user,
        finances: persona.finances,
        goals: persona.goals,
        targetAmount: persona.targetAmount,
        targetTimeline: persona.targetTimeline,
      })
    );
    localStorage.setItem("finsafe_onboarding_completed", "true");
    router.push("/dashboard");
  }

  async function handleLogin(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setIsLoading(true);
    setMessage("");

    const normalizedEmail = email.trim().toLowerCase();

    // 1. Try Java Spring Boot backend if available
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: normalizedEmail, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem("finsafe_token", data.token);
        localStorage.setItem("finsafe_user", JSON.stringify({
          name: data.fullName,
          email: data.email,
        }));

        // Load their financial context from backend
        const dashRes = await fetch(`${API_BASE_URL}/api/dashboard`, {
          headers: { Authorization: `Bearer ${data.token}` },
        });

        if (dashRes.ok) {
          const dashData = await dashRes.json();
          localStorage.setItem("finsafe_onboarding", JSON.stringify({
            user: { name: dashData.fullName, email: dashData.email },
            finances: {
              monthlyIncome: dashData.monthlyIncome,
              currentBalance: dashData.currentBalance,
              monthlyExpenses: dashData.monthlyExpenses,
              monthlyEmi: dashData.monthlyEmi,
            },
            goals: (dashData.goals || []).map((g: { name: string }) => g.name),
            targetAmount: 200000,
            targetTimeline: "12 Months",
          }));
          localStorage.setItem("finsafe_onboarding_completed", "true");
        }

        router.push("/dashboard");
        return;
      }
    } catch {
      // Backend not currently reachable; fallback to demo persona or local credentials
    }

    // 2. Check if logging in as one of the pre-seeded demo users
    if (DEMO_PERSONAS[normalizedEmail]) {
      logInWithPersona(normalizedEmail);
      return;
    }

    // 3. Fallback for any other user
    const savedUserRaw = localStorage.getItem("finsafe_user");
    if (savedUserRaw) {
      try {
        const savedUser = JSON.parse(savedUserRaw);
        if (savedUser.email === normalizedEmail) {
          router.push("/dashboard");
          return;
        }
      } catch {
        // ignore JSON parse error
      }
    }

    // Default: Log in and route to dashboard
    localStorage.setItem(
      "finsafe_user",
      JSON.stringify({ name: email.split("@")[0], email: normalizedEmail })
    );
    router.push("/dashboard");
  }

  return (
    <main className="auth-page">
      {/* LEFT SIDE - LOGIN FORM */}
      <section className="auth-left">
        <Link href="/" className="auth-brand">
          <ShieldCheck size={34} />
          <span>FinSafe</span>
        </Link>

        <div className="auth-form-container">
          <div className="auth-heading">
            <span className="auth-eyebrow">WELCOME BACK</span>
            <h1>Good to see you<br />again!</h1>
            <p>
              Log in to your account and continue your journey towards smarter financial decisions.
            </p>
          </div>

          {/* QUICK DEMO LOGIN BUTTONS - HIGH CONTRAST */}
          <div style={{
            marginBottom: 24,
            background: "#f0fdf4",
            padding: 16,
            borderRadius: 14,
            border: "1.5px solid #86efac",
            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.04)"
          }}>
            <div style={{
              fontSize: 12,
              fontWeight: 700,
              color: "#166534",
              display: "flex",
              alignItems: "center",
              gap: 6,
              marginBottom: 12,
              letterSpacing: "0.5px",
              textTransform: "uppercase"
            }}>
              <Zap size={15} color="#16a34a" /> 1-Click Demo Login (Test Personas)
            </div>
            
            <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
              <button
                type="button"
                onClick={() => logInWithPersona("aarav.sharma@example.com")}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  padding: "10px 14px",
                  borderRadius: 10,
                  border: "1.5px solid #86efac",
                  background: "#ffffff",
                  color: "#0f172a",
                  fontSize: 13,
                  fontWeight: 500,
                  cursor: "pointer",
                  textAlign: "left",
                  boxShadow: "0 1px 3px rgba(0, 0, 0, 0.06)",
                  transition: "all 0.15s ease",
                }}
              >
                <div>
                  <span style={{ fontWeight: 700, color: "#15803d" }}>🟢 Aarav Sharma</span>
                  <span style={{ color: "#475569", marginLeft: 6, fontSize: 12 }}>— Healthy (₹2.5L Balance)</span>
                </div>
                <ArrowRight size={15} color="#15803d" />
              </button>

              <button
                type="button"
                onClick={() => logInWithPersona("priya.patel@example.com")}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  padding: "10px 14px",
                  borderRadius: 10,
                  border: "1.5px solid #fde047",
                  background: "#ffffff",
                  color: "#0f172a",
                  fontSize: 13,
                  fontWeight: 500,
                  cursor: "pointer",
                  textAlign: "left",
                  boxShadow: "0 1px 3px rgba(0, 0, 0, 0.06)",
                  transition: "all 0.15s ease",
                }}
              >
                <div>
                  <span style={{ fontWeight: 700, color: "#b45309" }}>🟡 Priya Patel</span>
                  <span style={{ color: "#475569", marginLeft: 6, fontSize: 12 }}>— High Loans (₹42k Balance)</span>
                </div>
                <ArrowRight size={15} color="#b45309" />
              </button>

              <button
                type="button"
                onClick={() => logInWithPersona("rahul.verma@example.com")}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  padding: "10px 14px",
                  borderRadius: 10,
                  border: "1.5px solid #fca5a5",
                  background: "#ffffff",
                  color: "#0f172a",
                  fontSize: 13,
                  fontWeight: 500,
                  cursor: "pointer",
                  textAlign: "left",
                  boxShadow: "0 1px 3px rgba(0, 0, 0, 0.06)",
                  transition: "all 0.15s ease",
                }}
              >
                <div>
                  <span style={{ fontWeight: 700, color: "#b91c1c" }}>🔴 Rahul Verma</span>
                  <span style={{ color: "#475569", marginLeft: 6, fontSize: 12 }}>— Deficit (₹9.5k Balance)</span>
                </div>
                <ArrowRight size={15} color="#b91c1c" />
              </button>
            </div>
          </div>

          <div className="auth-divider">
            <span />
            <p>OR LOG IN WITH CREDENTIALS</p>
            <span />
          </div>

          {/* LOGIN FORM */}
          <form className="auth-form" onSubmit={handleLogin}>
            <label htmlFor="email">Email Address</label>
            <div className="auth-input-wrapper">
              <Mail size={19} />
              <input
                id="email"
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  setMessage("");
                }}
                required
              />
            </div>

            <div className="password-label-row">
              <label htmlFor="password">Password</label>
              <Link href="/forgot-password">Forgot password?</Link>
            </div>

            <div className="auth-input-wrapper">
              <LockKeyhole size={19} />
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  setMessage("");
                }}
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? <EyeOff size={19} /> : <Eye size={19} />}
              </button>
            </div>

            <button type="submit" className="auth-submit" disabled={isLoading}>
              {isLoading ? "Signing in..." : "Log In"}
              <ArrowRight size={18} />
            </button>

            {message && (
              <p className="auth-message" role="status">
                {message}
              </p>
            )}
          </form>

          <p className="auth-switch">
            Don't have an account?
            <Link href="/register"> Create one</Link>
          </p>

          <div className="auth-security">
            <ShieldCheck size={17} />
            Your financial information stays private and secure.
          </div>
        </div>

        <p className="auth-copyright">
          © 2026 FinSafe. Plan Smart. Spend Safe.
        </p>
      </section>

      {/* RIGHT SIDE - BRAND VISUAL */}
      <section className="auth-right">
        <div className="auth-visual-content">
          <div className="visual-brand-tag">
            <Sparkles size={15} />
            YOUR FINANCIAL COMPANION
          </div>

          <h2>
            Take control.<br />
            Spend smarter.<br />
            <span>Live better.</span>
          </h2>

          <p className="visual-description">
            Your financial journey deserves more than guesswork. Make every decision count with FinSafe.
          </p>

          <div className="auth-finance-card">
            <div className="finance-card-header">
              <div className="finance-card-icon">
                <Wallet size={22} />
              </div>
              <span>SMART FINANCIAL PLANNING</span>
            </div>

            <h3>Every smart decision<br />builds a better future.</h3>
            <div className="finance-card-divider" />

            <div className="finance-feature">
              <span><CheckCircle2 size={18} /></span>
              Understand your spending
            </div>
            <div className="finance-feature">
              <span><ShieldCheck size={18} /></span>
              Evaluate purchases wisely
            </div>
            <div className="finance-feature">
              <span><TrendingUp size={18} /></span>
              Work towards your goals
            </div>
          </div>

          <div className="auth-quote">
            "A better financial future starts with the decisions you make today."
          </div>
        </div>

        <div className="auth-decoration decoration-one" />
        <div className="auth-decoration decoration-two" />

        <div className="auth-right-footer">
          <ShieldCheck size={17} />
          Plan Smart. Spend Safe.
        </div>
      </section>
    </main>
  );
}
