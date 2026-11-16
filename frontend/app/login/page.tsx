
"use client";

import { useState } from "react";
import Link from "next/link";
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
} from "lucide-react";

export default function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  function handleLogin(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setMessage("Login interface ready. Backend authentication will be connected next.");
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
            <span className="auth-eyebrow">
              WELCOME BACK
            </span>

            <h1>Good to see you<br />again!</h1>

            <p>
              Log in to your account and continue your
              journey towards smarter financial decisions.
            </p>
          </div>

          {/* GOOGLE LOGIN */}
          <button
            type="button"
            className="google-button"
            onClick={() => setMessage("Google authentication will be connected after backend integration.")}
          >
            <svg viewBox="0 0 48 48" width="20" height="20">
              <path fill="#4285F4" d="M43.6 24.5c0-1.4-.1-2.8-.4-4.1H24v7.8h11a9.4 9.4 0 0 1-4.1 6.2v5.1h6.6c3.9-3.6 6.1-8.8 6.1-15z"/>
              <path fill="#34A853" d="M24 44c5.5 0 10.1-1.8 13.5-4.9l-6.6-5.1c-1.8 1.2-4.1 2-6.9 2-5.3 0-9.8-3.6-11.4-8.4H5.8v5.3A20 20 0 0 0 24 44z"/>
              <path fill="#FBBC05" d="M12.6 27.6a12 12 0 0 1 0-7.2v-5.3H5.8a20 20 0 0 0 0 17.8l6.8-5.3z"/>
              <path fill="#EA4335" d="M24 12.1c3 0 5.7 1 7.8 3.1l5.8-5.8C34.1 6 29.5 4 24 4A20 20 0 0 0 5.8 15.1l6.8 5.3c1.6-4.8 6.1-8.3 11.4-8.3z"/>
            </svg>
            Continue with Google
          </button>

          <div className="auth-divider">
            <span />
            <p>OR CONTINUE WITH EMAIL</p>
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
              <Link href="/forgot-password">
                Forgot password?
              </Link>
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

            <button type="submit" className="auth-submit">
              Log In
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
            Take control.
            <br />
            Spend smarter.
            <br />
            <span>Live better.</span>
          </h2>

          <p className="visual-description">
            Your financial journey deserves more than
            guesswork. Make every decision count with FinSafe.
          </p>

          {/* FINANCIAL VISUAL CARD */}
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
            "A better financial future starts with
            the decisions you make today."
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
