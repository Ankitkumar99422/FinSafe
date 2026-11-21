
"use client";

import { useState } from "react";
import type { FormEvent } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

import {
  ShieldCheck,
  UserRound,
  Wallet,
  Target,
  ArrowRight,
  ArrowLeft,
  CheckCircle2,
  BriefcaseBusiness,
  IndianRupee,
  PiggyBank,
  Sparkles,
} from "lucide-react";

const goalOptions = [
  "Emergency Fund",
  "Buy a Laptop",
  "Buy a Vehicle",
  "Higher Education",
  "Travel",
  "Home Purchase",
  "Retirement",
  "Other",
];

export default function OnboardingPage() {
  const router = useRouter();

  const [step, setStep] = useState(1);
  const [message, setMessage] = useState("");

  const [profile, setProfile] = useState({
    age: "",
    occupation: "",
    employment: "",
  });

  const [finances, setFinances] = useState({
    monthlyIncome: "",
    currentBalance: "",
    monthlyExpenses: "",
    monthlyEmi: "",
  });

  const [goals, setGoals] = useState<string[]>([]);
  const [targetAmount, setTargetAmount] = useState("");
  const [targetTimeline, setTargetTimeline] = useState("");

  // Update personal information
  const updateProfile = (field: string, value: string) => {
    setProfile((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  // Update financial information
  const updateFinances = (field: string, value: string) => {
    setFinances((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  // Select or deselect financial goals
  const toggleGoal = (goal: string) => {
    setGoals((prev) =>
      prev.includes(goal)
        ? prev.filter((item) => item !== goal)
        : [...prev, goal]
    );
  };

  // Move to the next onboarding step
  function handleNext() {
    setMessage("");

    if (step === 1 && (!profile.occupation || !profile.employment)) {
      setMessage(
        "Please complete your occupation and employment status."
      );
      return;
    }

    if (step === 2) {
      const requiredValues = [
        finances.monthlyIncome,
        finances.currentBalance,
        finances.monthlyExpenses,
      ];

      if (requiredValues.some((value) => value.trim() === "")) {
        setMessage(
          "Please complete your income, balance and expenses."
        );
        return;
      }

      if (
        requiredValues.some(
          (value) => !Number.isFinite(Number(value)) || Number(value) < 0
        )
      ) {
        setMessage("Please enter valid financial amounts.");
        return;
      }
    }

    setStep((prev) => Math.min(prev + 1, 3));
  }

  // Save onboarding information and open dashboard
  async function handleFinish(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setMessage("");

    if (goals.length === 0) {
      setMessage("Please select at least one financial goal.");
      return;
    }

    if (
      !targetAmount ||
      !Number.isFinite(Number(targetAmount)) ||
      Number(targetAmount) <= 0 ||
      !targetTimeline
    ) {
      setMessage("Please enter a valid target amount and timeline.");
      return;
    }

    if (
      finances.monthlyEmi.trim() !== "" &&
      (!Number.isFinite(Number(finances.monthlyEmi)) ||
        Number(finances.monthlyEmi) < 0)
    ) {
      setMessage("Please enter a valid EMI amount.");
      return;
    }

    try {
      // Read the registered user's information.
      // Registration page should save it using "finsafe_user".
      const registrationRaw =
        localStorage.getItem("finsafe_user") ||
        localStorage.getItem("finsafe_registration");

      let registeredUser: {
        name?: string;
        fullName?: string;
        email?: string;
      } = {};

      if (registrationRaw) {
        try {
          registeredUser = JSON.parse(registrationRaw);
        } catch {
          console.warn("Could not read registered user information.");
        }
      }

      // Complete user financial profile
      const onboardingData = {
        user: {
          name:
            registeredUser.name ||
            registeredUser.fullName ||
            "",
          email: registeredUser.email || "",
        },

        profile: {
          ...profile,
          age: profile.age ? Number(profile.age) : null,
        },

        finances: {
          monthlyIncome: Number(finances.monthlyIncome),
          currentBalance: Number(finances.currentBalance),
          monthlyExpenses: Number(finances.monthlyExpenses),
          monthlyEmi: finances.monthlyEmi
            ? Number(finances.monthlyEmi)
            : 0,
        },

        goals,

        targetAmount: Number(targetAmount),
        targetTimeline,

        completedAt: new Date().toISOString(),
      };

      // Save the complete profile in browser storage
      localStorage.setItem(
        "finsafe_onboarding",
        JSON.stringify(onboardingData)
      );

      // Save to Supabase via Java Spring Boot backend if token is available
      const token = localStorage.getItem("finsafe_token");
      if (token) {
        try {
          await fetch("http://localhost:8080/api/financial-profile", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
              age: profile.age ? Number(profile.age) : 25,
              occupation: profile.occupation || "Working Professional",
              employmentStatus: profile.employmentStatus || "FULL_TIME",
              monthlyIncome: Number(finances.monthlyIncome),
              currentBalance: Number(finances.currentBalance),
              monthlyExpenses: Number(finances.monthlyExpenses),
              monthlyEmi: finances.monthlyEmi ? Number(finances.monthlyEmi) : 0,
              goals: goals.map((g) => ({
                goalName: g,
                targetAmount: Number(targetAmount) || 100000,
                savedAmount: 0,
                targetDate: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString().split("T")[0],
                priority: "HIGH",
              })),
            }),
          });
        } catch (apiErr) {
          console.warn("Could not sync profile to Supabase backend:", apiErr);
        }
      }

      // Mark onboarding as completed
      localStorage.setItem("finsafe_onboarding_completed", "true");

      // Redirect to personalized dashboard
      router.push("/dashboard");
    } catch (error) {
      console.error("Error saving onboarding data:", error);
      setMessage(
        "Unable to save your details. Please try again."
      );
    }
  }

  return (
    <main className="onboarding-page">

      {/* HEADER */}

      <header className="onboarding-header">
        <Link href="/" className="auth-brand">
          <ShieldCheck size={32} />
          <span>FinSafe</span>
        </Link>

        <span className="onboarding-secure">
          <ShieldCheck size={16} />
          Secure onboarding
        </span>
      </header>

      <section className="onboarding-container">

        {/* INTRODUCTION */}

        <div className="onboarding-intro">
          <span className="auth-eyebrow">
            LET'S PERSONALISE YOUR EXPERIENCE
          </span>

          <h1>
            Your finances,
            <br />
            <span>your way.</span>
          </h1>

          <p>
            Tell us a little about your financial situation.
            We'll use your information to help you make
            smarter financial decisions.
          </p>
        </div>

        {/* PROGRESS */}

        <div className="onboarding-progress">

          <div className="progress-top">
            <span>Getting started</span>
            <strong>Step {step} of 3</strong>
          </div>

          <div className="progress-bar">
            <div
              className="progress-bar-fill"
              style={{ width: `${(step / 3) * 100}%` }}
            />
          </div>

          <div className="onboarding-steps">

            <div className={step >= 1 ? "step-active" : ""}>
              <UserRound size={16} />
              Personal
            </div>

            <div className={step >= 2 ? "step-active" : ""}>
              <Wallet size={16} />
              Finances
            </div>

            <div className={step >= 3 ? "step-active" : ""}>
              <Target size={16} />
              Goals
            </div>

          </div>
        </div>

        {/* FORM CARD */}

        <form
          className="onboarding-card"
          onSubmit={handleFinish}
        >

          {/* STEP 1: PERSONAL INFORMATION */}

          {step === 1 && (
            <div className="onboarding-section">

              <div className="onboarding-section-heading">
                <div className="onboarding-icon">
                  <UserRound size={23} />
                </div>

                <div>
                  <h2>Let's get to know you</h2>
                  <p>Tell us a little about yourself.</p>
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="age">
                  Your Age (Optional)
                </label>

                <input
                  id="age"
                  type="number"
                  min="18"
                  max="100"
                  placeholder="Enter your age"
                  value={profile.age}
                  onChange={(e) =>
                    updateProfile("age", e.target.value)
                  }
                />
              </div>

              <div className="onboarding-field">
                <label htmlFor="occupation">
                  Occupation
                </label>

                <div className="onboarding-input">
                  <BriefcaseBusiness size={18} />

                  <select
                    id="occupation"
                    value={profile.occupation}
                    onChange={(e) =>
                      updateProfile("occupation", e.target.value)
                    }
                    required
                  >
                    <option value="">
                      Select your occupation
                    </option>
                    <option value="Student">Student</option>
                    <option value="Salaried">
                      Salaried Employee
                    </option>
                    <option value="Self Employed">
                      Self Employed
                    </option>
                    <option value="Business Owner">
                      Business Owner
                    </option>
                    <option value="Freelancer">
                      Freelancer
                    </option>
                    <option value="Homemaker">
                      Homemaker
                    </option>
                    <option value="Retired">Retired</option>
                    <option value="Other">Other</option>
                  </select>
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="employment">
                  Employment Status
                </label>

                <select
                  id="employment"
                  value={profile.employment}
                  onChange={(e) =>
                    updateProfile("employment", e.target.value)
                  }
                  required
                >
                  <option value="">
                    Select employment status
                  </option>
                  <option value="Employed">Employed</option>
                  <option value="Unemployed">Unemployed</option>
                  <option value="Student">Student</option>
                  <option value="Self Employed">
                    Self Employed
                  </option>
                  <option value="Retired">Retired</option>
                </select>
              </div>

            </div>
          )}

          {/* STEP 2: FINANCIAL INFORMATION */}

          {step === 2 && (
            <div className="onboarding-section">

              <div className="onboarding-section-heading">
                <div className="onboarding-icon">
                  <Wallet size={23} />
                </div>

                <div>
                  <h2>Your financial snapshot</h2>
                  <p>
                    Enter your current financial details in INR.
                  </p>
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="income">
                  Monthly Income (₹)
                </label>

                <div className="onboarding-input">
                  <IndianRupee size={18} />

                  <input
                    id="income"
                    type="number"
                    min="0"
                    step="0.01"
                    placeholder="e.g. 45000"
                    value={finances.monthlyIncome}
                    onChange={(e) =>
                      updateFinances(
                        "monthlyIncome",
                        e.target.value
                      )
                    }
                    required
                  />
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="balance">
                  Current Available Balance (₹)
                </label>

                <div className="onboarding-input">
                  <Wallet size={18} />

                  <input
                    id="balance"
                    type="number"
                    min="0"
                    step="0.01"
                    placeholder="Enter your current balance"
                    value={finances.currentBalance}
                    onChange={(e) =>
                      updateFinances(
                        "currentBalance",
                        e.target.value
                      )
                    }
                    required
                  />
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="expenses">
                  Monthly Expenses (₹)
                </label>

                <div className="onboarding-input">
                  <IndianRupee size={18} />

                  <input
                    id="expenses"
                    type="number"
                    min="0"
                    step="0.01"
                    placeholder="Your average monthly expenses"
                    value={finances.monthlyExpenses}
                    onChange={(e) =>
                      updateFinances(
                        "monthlyExpenses",
                        e.target.value
                      )
                    }
                    required
                  />
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="emi">
                  Monthly Loan / EMI Payments (₹)
                  <span className="optional-label">
                    Optional
                  </span>
                </label>

                <div className="onboarding-input">
                  <IndianRupee size={18} />

                  <input
                    id="emi"
                    type="number"
                    min="0"
                    step="0.01"
                    placeholder="Enter 0 if you have none"
                    value={finances.monthlyEmi}
                    onChange={(e) =>
                      updateFinances(
                        "monthlyEmi",
                        e.target.value
                      )
                    }
                  />
                </div>
              </div>

              <div className="onboarding-note">
                <ShieldCheck size={17} />
                Your financial details are saved locally
                in this browser for now.
              </div>

            </div>
          )}

          {/* STEP 3: FINANCIAL GOALS */}

          {step === 3 && (
            <div className="onboarding-section">

              <div className="onboarding-section-heading">
                <div className="onboarding-icon">
                  <Target size={23} />
                </div>

                <div>
                  <h2>What are you saving for?</h2>
                  <p>
                    Select the goals that matter to you.
                  </p>
                </div>
              </div>

              <div className="goal-selection">

                {goalOptions.map((goal) => (
                  <button
                    type="button"
                    key={goal}
                    className={`goal-option ${
                      goals.includes(goal)
                        ? "goal-selected"
                        : ""
                    }`}
                    onClick={() => toggleGoal(goal)}
                    aria-pressed={goals.includes(goal)}
                  >
                    {goals.includes(goal) && (
                      <CheckCircle2 size={16} />
                    )}

                    {goal}
                  </button>
                ))}

              </div>

              <div className="onboarding-field">
                <label htmlFor="target-amount">
                  Your Target Savings Amount (₹)
                </label>

                <div className="onboarding-input">
                  <PiggyBank size={18} />

                  <input
                    id="target-amount"
                    type="number"
                    min="1"
                    step="0.01"
                    placeholder="e.g. 100000"
                    value={targetAmount}
                    onChange={(e) =>
                      setTargetAmount(e.target.value)
                    }
                    required
                  />
                </div>
              </div>

              <div className="onboarding-field">
                <label htmlFor="timeline">
                  Target Timeline
                </label>

                <select
                  id="timeline"
                  value={targetTimeline}
                  onChange={(e) =>
                    setTargetTimeline(e.target.value)
                  }
                  required
                >
                  <option value="">
                    When do you want to achieve it?
                  </option>
                  <option value="3 months">
                    Within 3 months
                  </option>
                  <option value="6 months">
                    Within 6 months
                  </option>
                  <option value="1 year">
                    Within 1 year
                  </option>
                  <option value="2 years">
                    Within 2 years
                  </option>
                  <option value="3+ years">
                    3+ years
                  </option>
                </select>
              </div>

              <div className="onboarding-note">
                <Sparkles size={17} />
                You can update your financial goals anytime.
              </div>

              <div className="onboarding-note">
                <ShieldCheck size={17} />
                Your financial profile will be saved
                in this browser.
              </div>

            </div>
          )}

          {/* ERROR / SUCCESS MESSAGE */}

          {message && (
            <div
              className="onboarding-message"
              role="status"
            >
              {message}
            </div>
          )}

          {/* NAVIGATION BUTTONS */}

          <div className="onboarding-actions">

            {step > 1 ? (
              <button
                type="button"
                className="onboarding-back"
                onClick={() => {
                  setMessage("");
                  setStep((prev) => prev - 1);
                }}
              >
                <ArrowLeft size={17} />
                Back
              </button>
            ) : (
              <Link
                href="/register"
                className="onboarding-back"
              >
                <ArrowLeft size={17} />
                Back
              </Link>
            )}

            {step < 3 ? (
              <button
                type="button"
                className="onboarding-next"
                onClick={handleNext}
              >
                Continue
                <ArrowRight size={17} />
              </button>
            ) : (
              <button
                type="submit"
                className="onboarding-next"
              >
                Complete Setup
                <CheckCircle2 size={17} />
              </button>
            )}

          </div>

        </form>

        <p className="onboarding-footer">
          <ShieldCheck size={15} />
          FinSafe · Plan Smart. Spend Safe.
        </p>

      </section>
    </main>
  );
}
