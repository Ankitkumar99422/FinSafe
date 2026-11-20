from app.finance.decision_config import MIN_SAFETY_BALANCE


def check_affordability(forecast, purchase_amount):
    if purchase_amount <= 0:
        raise ValueError("purchase_amount must be positive")
    after_typical = forecast["projected_balance_typical"] - purchase_amount
    after_bad = forecast["projected_balance_bad_case"] - purchase_amount
    if after_bad >= MIN_SAFETY_BALANCE:
        verdict = "affordable"
    elif after_typical >= MIN_SAFETY_BALANCE:
        verdict = "risky"
    else:
        verdict = "not_affordable"
    return {
        "verdict": verdict,
        "purchase_amount": float(purchase_amount),
        "min_safety_balance": MIN_SAFETY_BALANCE,
        "balance_after_typical": round(after_typical, 2),
        "balance_after_bad_case": round(after_bad, 2),
        "shortfall_typical": round(max(0.0, MIN_SAFETY_BALANCE - after_typical), 2),
        "shortfall_bad_case": round(max(0.0, MIN_SAFETY_BALANCE - after_bad), 2),
        "max_safe_purchase": round(max(0.0, forecast["projected_balance_bad_case"] - MIN_SAFETY_BALANCE), 2),
        "forecast_status": forecast["forecast_status"],
        "uncertain_items": forecast["uncertain_items"],
    }
