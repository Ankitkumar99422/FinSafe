
"use client";

import { useState } from "react";
import type { FormEvent } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

import {
  ShieldCheck,
  Eye,
  EyeOff,
  Mail,
  LockKeyhole,
  UserRound,
  ArrowRight,
  CheckCircle2,
  Wallet,
  TrendingUp,
  Sparkles,
} from "lucide-react";
import { API_BASE_URL } from "@/lib/api";

export default function RegisterPage() {
  const router = useRouter();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [agree, setAgree] = useState(false);
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleRegister(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setMessage("");

    // Validate name
    if (!name.trim()) {
      setMessage("Please enter your full name.");
      return;
    }

    // Validate password
    if (password.length < 8) {
      setMessage("Password must contain at least 8 characters.");
      return;
    }

    if (password !== confirmPassword) {
      setMessage("Passwords do not match. Please try again.");
      return;
    }

    // Validate terms acceptance
    if (!agree) {
      setMessage("Please accept the Terms and Conditions.");
      return;
    }

    setIsLoading(true);

    try {
      const normalizedEmail = email.trim().toLowerCase();

      // Send registration request to Java Spring Boot backend -> writes to Supabase
      const res = await fetch(`${API_BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          fullName: name.trim(),
          email: normalizedEmail,
          password: password,
          age: 25,
          occupation: "Working Professional",
          employmentStatus: "FULL_TIME",
        }),
      });

      if (!res.ok) {
        let errorMsg = "Registration failed. Email may already be in use.";
        try {
          const errData = await res.json();
          if (errData && errData.message) errorMsg = errData.message;
        } catch {
          // keep fallback
        }
        throw new Error(errorMsg);
      }

      const data = await res.json();

      // Store JWT token and user info
      localStorage.setItem("finsafe_token", data.token);
      localStorage.setItem(
        "finsafe_user",
        JSON.stringify({
          name: data.fullName,
          email: data.email,
          userId: data.userId,
        })
      );

      // Clear any previous onboarding data
      localStorage.removeItem("finsafe_onboarding");
      localStorage.removeItem("finsafe_onboarding_completed");

      // Redirect to onboarding
      router.push("/onboarding");
    } catch (error: any) {
      console.error("Registration error:", error);
      setMessage(error?.message || "Unable to register user in database. Please check your backend.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <main className="auth-page">

      {/* LEFT SIDE - REGISTRATION */}

      <section className="auth-left">

        <Link href="/" className="auth-brand">
          <ShieldCheck size={34} />
          <span>FinSafe</span>
        </Link>

        <div className="auth-form-container register-container">

          <div className="auth-heading">
            <span className="auth-eyebrow">
              GET STARTED WITH FINSAFE
            </span>

            <h1>
              Create your
              <br />
              account.
            </h1>

            <p>
              Start your journey towards better financial
              decisions. Your smarter financial future begins here.
            </p>
          </div>

          {/* GOOGLE SIGNUP */}

          <button
            type="button"
            className="google-button"
            onClick={() =>
              setMessage(
                "Google registration will be connected after backend integration."
              )
            }
          >
            <svg viewBox="0 0 48 48" width="20" height="20">
              <path
                fill="#4285F4"
                d="M43.6 24.5c0-1.4-.1-2.8-.4-4.1H24v7.8h11a9.4 9.4 0 0 1-4.1 6.2v5.1h6.6c3.9-3.6 6.1-8.8 6.1-15z"
              />
              <path
                fill="#34A853"
                d="M24 44c5.5 0 10.1-1.8 13.5-4.9l-6.6-5.1c-1.8 1.2-4.1 2-6.9 2-5.3 0-9.8-3.6-11.4-8.4H5.8v5.3A20 20 0 0 0 24 44z"
              />
              <path
                fill="#FBBC05"
                d="M12.6 27.6a12 12 0 0 1 0-7.2v-5.3H5.8a20 20 0 0 0 0 17.8l6.8-5.3z"
              />
              <path
                fill="#EA4335"
                d="M24 12.1c3 0 5.7 1 7.8 3.1l5.8-5.8C34.1 6 29.5 4 24 4A20 20 0 0 0 5.8 15.1l6.8 5.3c1.6-4.8 6.1-8.3 11.4-8.3z"
              />
            </svg>

            Continue with Google
          </button>

          <div className="auth-divider">
            <span />
            <p>OR REGISTER WITH EMAIL</p>
            <span />
          </div>

          {/* REGISTRATION FORM */}

          <form
            className="auth-form"
            onSubmit={handleRegister}
          >

            <label htmlFor="full-name">
              Full Name
            </label>

            <div className="auth-input-wrapper">
              <UserRound size={19} />

              <input
                id="full-name"
                type="text"
                placeholder="Enter your full name"
                value={name}
                onChange={(e) => {
                  setName(e.target.value);
                  setMessage("");
                }}
                autoComplete="name"
                required
              />
            </div>

            <label htmlFor="register-email">
              Email Address
            </label>

            <div className="auth-input-wrapper">
              <Mail size={19} />

              <input
                id="register-email"
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  setMessage("");
                }}
                autoComplete="email"
                required
              />
            </div>

            <label htmlFor="register-password">
              Password
            </label>

            <div className="auth-input-wrapper">
              <LockKeyhole size={19} />

              <input
                id="register-password"
                type={showPassword ? "text" : "password"}
                placeholder="Create a password"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  setMessage("");
                }}
                autoComplete="new-password"
                minLength={8}
                required
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() =>
                  setShowPassword(!showPassword)
                }
                aria-label={
                  showPassword ? "Hide password" : "Show password"
                }
              >
                {showPassword ? (
                  <EyeOff size={19} />
                ) : (
                  <Eye size={19} />
                )}
              </button>
            </div>

            <label htmlFor="confirm-password">
              Confirm Password
            </label>

            <div className="auth-input-wrapper">
              <LockKeyhole size={19} />

              <input
                id="confirm-password"
                type={showConfirm ? "text" : "password"}
                placeholder="Re-enter your password"
                value={confirmPassword}
                onChange={(e) => {
                  setConfirmPassword(e.target.value);
                  setMessage("");
                }}
                autoComplete="new-password"
                minLength={8}
                required
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() =>
                  setShowConfirm(!showConfirm)
                }
                aria-label={
                  showConfirm
                    ? "Hide password"
                    : "Show password"
                }
              >
                {showConfirm ? (
                  <EyeOff size={19} />
                ) : (
                  <Eye size={19} />
                )}
              </button>
            </div>

            {/* TERMS AND CONDITIONS */}

            <label className="terms-row">
              <input
                type="checkbox"
                checked={agree}
                onChange={(e) => {
                  setAgree(e.target.checked);
                  setMessage("");
                }}
              />

              <span>
                I agree to the{" "}
                <Link href="/terms">
                  Terms & Conditions
                </Link>
                {" "}and{" "}
                <Link href="/privacy">
                  Privacy Policy
                </Link>.
              </span>
            </label>

            <button
              type="submit"
              className="auth-submit"
              disabled={isLoading}
            >
              {isLoading ? "Creating Account..." : "Create Account"}
              <ArrowRight size={18} />
            </button>

            {message && (
              <p className="auth-message" role="alert">
                {message}
              </p>
            )}

          </form>

          <p className="auth-switch">
            Already have an account?
            <Link href="/login"> Log in</Link>
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
            YOUR FINANCIAL JOURNEY STARTS HERE
          </div>

          <h2>
            Start smart.
            <br />
            Plan better.
            <br />
            <span>Grow together.</span>
          </h2>

          <p className="visual-description">
            Every financial journey begins with a single step.
            Create your account and take charge of your financial future.
          </p>

          <div className="auth-finance-card">

            <div className="finance-card-header">
              <div className="finance-card-icon">
                <Wallet size={22} />
              </div>

              <span>WELCOME TO FINSAFE</span>
            </div>

            <h3>
              Your goals.
              <br />
              Your money.
              <br />
              Your decisions.
            </h3>

            <div className="finance-card-divider" />

            <div className="finance-feature">
              <span>
                <CheckCircle2 size={18} />
              </span>
              Personalised financial dashboard
            </div>

            <div className="finance-feature">
              <span>
                <ShieldCheck size={18} />
              </span>
              Smarter purchase decisions
            </div>

            <div className="finance-feature">
              <span>
                <TrendingUp size={18} />
              </span>
              Track your financial progress
            </div>

          </div>

          <div className="auth-quote">
            "Small steps today. A financially stronger tomorrow."
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
