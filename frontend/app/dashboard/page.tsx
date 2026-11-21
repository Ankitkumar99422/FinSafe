
"use client";

import { useEffect, useState, type FormEvent } from "react";
import {
  ShieldCheck,
  LayoutDashboard,
  UsersRound,
  FileText,
  Target,
  Settings,
  CircleHelp,
  Search,
  Bell,
  ChevronDown,
  Wallet,
  Eye,
  EyeOff,
  ChartColumn,
  HeartPulse,
  CalendarDays,
  ArrowRight,
  Sparkles,
  Laptop,
  Smartphone,
  Plane,
  Bike,
  CheckCircle,
  Clock,
  Zap,
  User,
  TrendingDown,
  Info,
  ShoppingCart,
  Menu,
  X,
} from "lucide-react";
import { API_BASE_URL } from "@/lib/api";

// TYPES

type UserData = {
  name?: string;
  fullName?: string;
  email?: string;
};

type OnboardingData = {
  profile?: {
    age?: number;
    occupation?: string;
    employment?: string;
  };
  finances?: {
    monthlyIncome?: number;
    currentBalance?: number;
    monthlyExpenses?: number;
    monthlyEmi?: number;
  };
  goals?: string[];
  targetAmount?: number;
  targetTimeline?: string;
};

type GoalItem = {
  name: string;
  image: string;
};

const navigation = [
  { name: "Dashboard", icon: LayoutDashboard, href: "#dashboard" },
  { name: "Analyze Purchase", icon: UsersRound, href: "#analyzer" },
  { name: "Financial History", icon: FileText, href: "#history" },
  { name: "Goals", icon: Target, href: "#goals" },
  { name: "Settings", icon: Settings, href: "#settings" },
  { name: "Help & Support", icon: CircleHelp, href: "#help" },
];

const suggestions = [
  { name: "Laptop", icon: Laptop },
  { name: "Phone", icon: Smartphone },
  { name: "Vacation", icon: Plane },
  { name: "Bike", icon: Bike },
];

const goalImages: Record<string, string> = {
  "Emergency Fund":
    "https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?w=300",
  "Buy a Laptop":
    "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=300",
  "Buy a Vehicle":
    "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=300",
  "Higher Education":
    "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=300",
  Travel:
    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=300",
  "Home Purchase":
    "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=300",
  Retirement:
    "https://images.unsplash.com/photo-1470252649378-9c29740c9fa8?w=300",
  Other:
    "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=300",
};

// HELPERS

function formatCurrency(value: number) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(value || 0);
}

function getInitials(name: string) {
  return (
    name
      .trim()
      .split(/\s+/)
      .map((part) => part[0])
      .join("")
      .slice(0, 2)
      .toUpperCase() || "U"
  );
}

function getGoalImage(goal: string) {
  return (
    goalImages[goal] ||
    "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=300"
  );
}

// DASHBOARD

export default function Dashboard() {
  const [purchase, setPurchase] = useState("");
  const [showMessage, setShowMessage] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [showBalance, setShowBalance] = useState(true);

  const [user, setUser] = useState<UserData>({});
  const [onboarding, setOnboarding] = useState<OnboardingData>({});
  const [isLoaded, setIsLoaded] = useState(false);

  // LOAD SAVED USER DATA

  useEffect(() => {
    try {
      const savedUser = localStorage.getItem("finsafe_user");
      const savedOnboarding = localStorage.getItem(
        "finsafe_onboarding"
      );

      if (savedUser) {
        setUser(JSON.parse(savedUser));
      }

      if (savedOnboarding) {
        setOnboarding(JSON.parse(savedOnboarding));
      }

      // Check for live Supabase sync via Java Spring Boot backend
      const token = localStorage.getItem("finsafe_token");
      if (token) {
        fetch(`${API_BASE_URL}/api/dashboard`, {
          headers: { Authorization: `Bearer ${token}` },
        })
          .then((res) => (res.ok ? res.json() : null))
          .then((dashData) => {
            if (dashData) {
              setUser((prev: any) => ({
                ...prev,
                name: dashData.fullName || prev?.name,
                email: dashData.email || prev?.email,
              }));
              setOnboarding((prev: any) => ({
                ...prev,
                finances: {
                  monthlyIncome: dashData.monthlyIncome,
                  currentBalance: dashData.currentBalance,
                  monthlyExpenses: dashData.monthlyExpenses,
                  monthlyEmi: dashData.monthlyEmi,
                },
                goals: (dashData.goals && dashData.goals.length > 0)
                  ? dashData.goals.map((g: any) => g.name)
                  : prev?.goals || [],
              }));
            }
          })
          .catch((err) => console.warn("Live dashboard sync error:", err));
      }
    } catch (error) {
      console.error("Unable to load FinSafe user data:", error);
    } finally {
      setIsLoaded(true);
    }
  }, []);

  // USER DETAILS

  const userName =
    user.name || user.fullName || "User";

  const firstName = userName.trim().split(/\s+/)[0];

  const initials = getInitials(userName);

  // FINANCIAL DETAILS

  const finances = onboarding.finances || {};

  const monthlyIncome = Number(finances.monthlyIncome) || 0;

  const currentBalance = Number(finances.currentBalance) || 0;

  const monthlyExpenses = Number(finances.monthlyExpenses) || 0;

  const monthlyEmi = Number(finances.monthlyEmi) || 0;

  const monthlySurplus =
    monthlyIncome - monthlyExpenses - monthlyEmi;

  const savingsGoals = onboarding.goals || [];

  const targetAmount = Number(onboarding.targetAmount) || 0;

  const targetTimeline = onboarding.targetTimeline || "";

  const bills = monthlyEmi > 0
    ? [
        {
          name: "Monthly Loan / EMI",
          amount: formatCurrency(monthlyEmi),
          date: "Monthly commitment",
          icon: "₹",
          color: "#35a77c",
        },
      ]
    : [];

  // PURCHASE ANALYZER

  function handleAnalyze(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();

    if (!purchase.trim()) return;

    setShowMessage(true);
  }

  // LOADING STATE

  if (!isLoaded) {
    return (
      <main className="dashboard-shell">
        <div style={{ padding: 50, textAlign: "center" }}>
          Loading your FinSafe dashboard...
        </div>
      </main>
    );
  }

  return (
    <main className="dashboard-shell" id="dashboard">

      {/* SIDEBAR */}

      <aside
        className={`sidebar ${
          sidebarOpen ? "sidebar-open" : ""
        }`}
      >
        <div className="brand">
          <div className="brand-name">
            <ShieldCheck size={32} className="brand-logo" />
            FinSafe
          </div>

          <div className="brand-tagline">
            Plan Smart. Spend Safe.
          </div>

          <button
            className="mobile-close"
            onClick={() => setSidebarOpen(false)}
            aria-label="Close menu"
          >
            <X size={21} />
          </button>
        </div>

        <nav className="nav-list">
          {navigation.map((item, index) => {
            const Icon = item.icon;

            return (
              <a
                href={item.href}
                key={item.name}
                onClick={() => setSidebarOpen(false)}
                className={`nav-item ${
                  index === 0 ? "active" : ""
                }`}
              >
                <Icon size={20} />
                <span>{item.name}</span>
              </a>
            );
          })}
        </nav>

        <div className="sidebar-footer">
          Your Financial
          <br />
          Well-being, Our Priority.
          <div className="footer-leaf">🌿</div>
        </div>
      </aside>

      {/* MAIN CONTENT */}

      <section className="main-content">

        {/* HEADER */}

        <header className="topbar">

          <button
            className="mobile-menu-button"
            onClick={() => setSidebarOpen(true)}
            aria-label="Open menu"
          >
            <Menu size={22} />
          </button>

          <div className="search-box">
            <Search size={20} />
            <input placeholder="Search anything..." />
          </div>

          <div className="profile-area">

            <button
              className="icon-button"
              aria-label="Notifications"
            >
              <Bell size={22} />
              <span className="notification-dot" />
            </button>

            <div className="profile">
              <div className="avatar">{initials}</div>

              <span className="profile-name">
                {userName}
              </span>

              <ChevronDown size={15} />
            </div>

          </div>
        </header>

        {/* HERO SECTION */}

        <section className="hero-section">

          <div className="hero-main">

            <h1 className="welcome-title">
              Welcome, {firstName}! 👋
            </h1>

            <p className="welcome-subtitle">
              Take control of your finances, one smart
              decision at a time.
            </p>

            {/* BALANCE AND INCOME */}

            <div className="stats-strip">

              <Wallet size={39} color="#53c99c" />

              <div className="stat-item">

                <div className="stat-label">
                  Available Balance
                </div>

                <div className="stat-value">
                  {showBalance
                    ? formatCurrency(currentBalance)
                    : "••••••"}

                  <button
                    type="button"
                    onClick={() =>
                      setShowBalance(!showBalance)
                    }
                    aria-label={
                      showBalance
                        ? "Hide balance"
                        : "Show balance"
                    }
                    style={{
                      background: "none",
                      border: "none",
                      color: "inherit",
                      cursor: "pointer",
                      marginLeft: 8,
                    }}
                  >
                    {showBalance ? (
                      <Eye size={17} />
                    ) : (
                      <EyeOff size={17} />
                    )}
                  </button>
                </div>

              </div>

              <div className="stat-divider" />

              <div className="stat-item">

                <div className="stat-label">
                  Monthly Income
                </div>

                <div className="stat-value">
                  {formatCurrency(monthlyIncome)}
                </div>

              </div>

            </div>

          </div>

          <div className="hero-aside">
            <p className="quote">
              "A better financial future
              <br />
              is in your hands."
            </p>
          </div>

        </section>

        {/* OVERVIEW ROW */}

        <section className="overview-layout">

          <div className="dashboard-grid">

            {/* SPENDING OVERVIEW */}

            <div className="panel spending-panel">

              <div className="panel-heading">

                <div className="heading-left">
                  <ChartColumn
                    className="heading-icon"
                    size={23}
                  />
                  Spending Overview
                </div>

                <span className="small-select">
                  This Month
                </span>

              </div>

              <div className="spending-content">

                <div className="donut-chart">
                  <div className="donut-inner">
                    <strong>
                      {formatCurrency(monthlyExpenses)}
                    </strong>
                    <span>Spent</span>
                  </div>
                </div>

                <div className="legend">

                  <div className="legend-row">
                    <span className="legend-name">
                      <span
                        className="legend-dot"
                        style={{ background: "#e36b7c" }}
                      />
                      Expenses
                    </span>

                    <strong>
                      {formatCurrency(monthlyExpenses)}
                    </strong>
                  </div>

                  <div className="legend-row">
                    <span className="legend-name">
                      <span
                        className="legend-dot"
                        style={{ background: "#f3c348" }}
                      />
                      EMI
                    </span>

                    <strong>
                      {formatCurrency(monthlyEmi)}
                    </strong>
                  </div>

                  <div className="legend-row">
                    <span className="legend-name">
                      <span
                        className="legend-dot"
                        style={{ background: "#35af7b" }}
                      />
                      Monthly Income
                    </span>

                    <strong>
                      {formatCurrency(monthlyIncome)}
                    </strong>
                  </div>

                </div>

              </div>

              <div className="spending-footer">
                <span className="green-pill">
                  <TrendingDown size={14} />
                  Your monthly spending
                </span>
              </div>

            </div>

            {/* FINANCIAL HEALTH / CASH FLOW */}

            <div className="panel health-panel">

              <div className="panel-heading">

                <div className="heading-left">
                  <HeartPulse
                    className="heading-icon"
                    size={24}
                  />
                  Your Financial Health
                </div>

                <Info size={18} color="#71857b" />

              </div>

              <div className="health-content">

                <div className="health-gauge" />

                <div className="health-score">
                  {formatCurrency(monthlySurplus)}
                </div>

                <div className="health-status">
                  <CheckCircle
                    size={17}
                    fill={
                      monthlySurplus >= 0
                        ? "#239b56"
                        : "#dc5555"
                    }
                    color="white"
                  />

                  {monthlySurplus >= 0
                    ? "Monthly Surplus"
                    : "Monthly Deficit"}
                </div>

                <p className="health-message">
                  Income minus expenses and EMI
                </p>

              </div>

            </div>

            {/* UPCOMING BILLS */}

            <div className="panel bills-panel">

              <div className="panel-heading">

                <div className="heading-left">
                  <CalendarDays
                    className="heading-icon"
                    size={22}
                  />
                  Monthly Commitments
                </div>

              </div>

              <div className="bill-list">

                {bills.length > 0 ? (
                  bills.map((bill) => (
                    <div
                      className="bill-row"
                      key={bill.name}
                    >

                      <div className="bill-name">

                        <span
                          className="bill-icon"
                          style={{ color: bill.color }}
                        >
                          {bill.icon}
                        </span>

                        {bill.name}

                      </div>

                      <div>
                        <div className="bill-amount">
                          {bill.amount}
                        </div>

                        <div className="bill-date">
                          {bill.date}
                        </div>
                      </div>

                    </div>
                  ))
                ) : (
                  <p style={{ padding: 20, color: "#71857b" }}>
                    No monthly EMI commitments added.
                  </p>
                )}

              </div>

            </div>

          </div>

          {/* GOALS PANEL */}

          <aside className="goals-panel" id="goals">

            <div className="panel-heading">

              <div className="heading-left">
                <Target size={25} />
                Your Goals
              </div>

            </div>

            <div style={{ marginBottom: 20 }}>
              <div className="goal-name">
                Total Savings Target
              </div>

              <div className="goal-amount">
                {formatCurrency(targetAmount)}
              </div>

              <div className="goal-footer">
                {targetTimeline
                  ? `Timeline: ${targetTimeline}`
                  : "Set your savings timeline"}
              </div>
            </div>

            {savingsGoals.length > 0 ? (
              savingsGoals.map((goal) => (
                <div
                  className="goal-item"
                  key={goal}
                >

                  <div
                    className="goal-image"
                    style={{
                      backgroundImage: `url(${getGoalImage(goal)})`,
                    }}
                  />

                  <div className="goal-info">

                    <div className="goal-name">
                      {goal}
                    </div>

                    <div className="goal-footer">
                      Added to your savings plan
                    </div>

                  </div>

                </div>
              ))
            ) : (
              <p style={{ color: "#d5e6df" }}>
                You haven't selected any goals yet.
              </p>
            )}

            <button className="outline-button">
              View All Goals <ArrowRight size={15} />
            </button>

          </aside>

        </section>

        {/* LOWER DASHBOARD ROW */}

        <section className="lower-grid">

          {/* PURCHASE ANALYZER */}

          <div
            className="panel analyzer-panel"
            id="analyzer"
          >

            <div className="panel-heading">

              <div className="heading-left">
                <Sparkles
                  size={26}
                  className="sparkle-icon"
                />

                <span className="analyzer-title">
                  Purchase Analyzer
                </span>

              </div>

            </div>

            <p className="analyzer-description">
              Check if your desired purchase is financially safe.
            </p>

            <form
              className="analyzer-form"
              onSubmit={handleAnalyze}
            >

              <ShoppingCart
                size={19}
                color="#426f5c"
              />

              <input
                value={purchase}
                onChange={(e) => {
                  setPurchase(e.target.value);
                  setShowMessage(false);
                }}
                placeholder="e.g. Laptop worth ₹80,000"
                aria-label="Enter your desired purchase"
              />

              <button
                type="submit"
                className="primary-button"
              >
                Analyze <ArrowRight size={15} />
              </button>

            </form>

            {showMessage && (
              <p className="analyzer-message">
                Purchase request received! Your financial
                information is ready for analysis.
                Connect the backend to generate a real
                purchase recommendation.
              </p>
            )}

            <div className="suggestions">

              Popular Suggestions:

              <div className="suggestion-list">

                {suggestions.map((item) => {
                  const Icon = item.icon;

                  return (
                    <button
                      type="button"
                      className="suggestion-chip"
                      key={item.name}
                      onClick={() => {
                        setPurchase(item.name);
                        setShowMessage(false);
                      }}
                    >
                      <Icon size={14} />
                      {item.name}
                    </button>
                  );
                })}

              </div>

            </div>

            <img
              className="analyzer-laptop"
              src="https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600"
              alt="Laptop"
            />

          </div>

          {/* FINANCIAL SUMMARY */}

          <div className="panel decision-panel">

            <div className="decision-top">

              <div className="decision-info">

                <span className="decision-label">
                  <CheckCircle
                    size={13}
                    fill="#269c53"
                    color="white"
                  />
                  Your Financial Summary
                </span>

                <h2 className="decision-title">
                  {monthlySurplus >= 0
                    ? "You're on track"
                    : "Review spending"}
                </h2>

                <p className="decision-description">
                  Your monthly income after accounting
                  for expenses and EMI.
                </p>

              </div>

              <div className="safety-score">
                <strong>
                  {formatCurrency(monthlySurplus)}
                </strong>

                <span>Monthly Surplus</span>
              </div>

            </div>

            <div className="check-row">

              <ChartColumn
                size={22}
                color="#249c67"
              />

              <div>
                <strong>Monthly Income</strong>
                <p>
                  {formatCurrency(monthlyIncome)}
                </p>
              </div>

              <ChevronDown size={15} />

            </div>

            <div className="check-row">

              <ShieldCheck
                size={22}
                color="#249c67"
              />

              <div>
                <strong>Monthly Expenses + EMI</strong>
                <p>
                  {formatCurrency(monthlyExpenses + monthlyEmi)}
                </p>
              </div>

              <ChevronDown size={15} />

            </div>

          </div>

          {/* RIGHT SIDE */}

          <div className="right-column">

            <div className="panel quick-panel">

              <div className="panel-heading">

                <div className="heading-left">
                  <Zap size={22} color="#35a77c" />
                  Quick Actions
                </div>

              </div>

              <div className="quick-actions">

                {[
                  {
                    name: "View Financial History",
                    icon: Clock,
                    href: "#history",
                  },
                  {
                    name: "Manage Goals",
                    icon: Target,
                    href: "#goals",
                  },
                  {
                    name: "Update Profile",
                    icon: User,
                    href: "#settings",
                  },
                  {
                    name: "Track Expenses",
                    icon: ChartColumn,
                    href: "#history",
                  },
                ].map((action) => {
                  const Icon = action.icon;

                  return (
                    <a
                      href={action.href}
                      className="quick-action"
                      key={action.name}
                    >

                      <span className="quick-action-label">
                        <Icon size={16} />
                        {action.name}
                      </span>

                      <ArrowRight size={14} />

                    </a>
                  );
                })}

              </div>

            </div>

            {/* PROMOTIONAL CARD */}

            <div className="promo-card">

              <div>
                <strong>Smarter Decisions.</strong>
                <br />
                A Healthier Tomorrow.
              </div>

              <div className="promo-plant">
                🌱
              </div>

            </div>

          </div>

        </section>

        <footer className="dashboard-footer">
          FinSafe © 2026 · Plan Smart. Spend Safe.
        </footer>

      </section>

    </main>
  );
}
