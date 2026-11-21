
import Link from "next/link";
import {
  ShieldCheck,
  ArrowRight,
  Wallet,
  ChartNoAxesCombined,
  Sparkles,
  LockKeyhole,
} from "lucide-react";

export default function Home() {
  return (
    <main className="landing-page">
      <nav className="landing-nav">
        <Link href="/" className="landing-brand">
          <ShieldCheck size={31} />
          <span>FinSafe</span>
        </Link>

        <div className="landing-nav-links">
          <Link href="#features">Features</Link>
          <Link href="#about">About</Link>
          <Link href="/login" className="nav-login">
            Log In
          </Link>
          <Link href="/register" className="nav-signup">
            Get Started <ArrowRight size={15} />
          </Link>
        </div>
      </nav>

      <section className="landing-hero">
        <div className="landing-content">
          <span className="landing-badge">
            <Sparkles size={14} />
            SMARTER FINANCIAL DECISIONS
          </span>

          <h1>
            Your Money.
            <br />
            Your Future.
            <br />
            <span>FinSafe.</span>
          </h1>

          <p>
            Take control of your finances, understand your
            spending habits, and make smarter purchasing
            decisions with your personal financial companion.
          </p>

          <div className="landing-buttons">
            <Link href="/register" className="landing-primary">
              Get Started <ArrowRight size={18} />
            </Link>

            <Link href="/login" className="landing-secondary">
              I already have an account
            </Link>
          </div>

          <div className="landing-trust">
            <LockKeyhole size={15} />
            Your finances. Your privacy. Your control.
          </div>
        </div>

        <div className="landing-visual">
          <div className="visual-card">
            <div className="visual-icon">
              <Wallet size={25} />
            </div>
            <span>Your financial journey</span>
            <h2>Starts with one smart decision.</h2>

            <div className="visual-features">
              <div>
                <ChartNoAxesCombined size={19} />
                <span>Track your spending</span>
              </div>
              <div>
                <ShieldCheck size={19} />
                <span>Make safer purchases</span>
              </div>
              <div>
                <Sparkles size={19} />
                <span>Build smarter habits</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="landing-features" id="features">
        <h2>Everything your finances need.</h2>
        <p>One place to understand and improve your financial life.</p>

        <div className="feature-grid">
          <article>
            <Wallet size={25} />
            <h3>Expense Tracking</h3>
            <p>Understand where your money goes.</p>
          </article>

          <article>
            <ShieldCheck size={25} />
            <h3>Purchase Analyzer</h3>
            <p>Make informed purchasing decisions.</p>
          </article>

          <article>
            <ChartNoAxesCombined size={25} />
            <h3>Financial Health</h3>
            <p>Monitor your financial progress.</p>
          </article>
        </div>
      </section>

      <footer className="landing-footer" id="about">
        FinSafe © 2026 · Plan Smart. Spend Safe.
      </footer>
    </main>
  );
}
