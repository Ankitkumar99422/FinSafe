from app.finance.affordability import check_affordability


def calculate_emi(principal, annual_rate_percent, tenure_months):
    if principal <= 0 or tenure_months <= 0:
        raise ValueError("principal and tenure_months must be positive")
    if annual_rate_percent < 0:
        raise ValueError("annual_rate_percent cannot be negative")
    r = annual_rate_percent / 12 / 100
    if r == 0:
        emi = principal / tenure_months
    else:
        growth = (1 + r) ** tenure_months
        emi = principal * r * growth / (growth - 1)
    total_paid = emi * tenure_months
    return {"emi": round(emi, 2), "total_paid": round(total_paid, 2),
            "total_interest": round(total_paid - principal, 2)}


def compare_payment_options(forecast, purchase_amount, emi_offers):
    options = []
    buy = check_affordability(forecast, purchase_amount)
    options.append({"option": "buy_now", "monthly_payment": 0.0, "total_cost": float(purchase_amount),
                    "extra_cost": 0.0, "verdict": buy["verdict"], "months_checked": 0,
                    "balance_after_bad_case": buy["balance_after_bad_case"]})
    for offer in emi_offers:
        calc = calculate_emi(purchase_amount, offer["annual_rate_percent"], offer["tenure_months"])
        first = check_affordability(forecast, calc["emi"])
        options.append({"option": "emi", "tenure_months": offer["tenure_months"],
                        "annual_rate_percent": offer["annual_rate_percent"],
                        "monthly_payment": calc["emi"], "total_cost": calc["total_paid"],
                        "extra_cost": calc["total_interest"], "verdict": first["verdict"],
                        "months_checked": 1,
                        "balance_after_bad_case": first["balance_after_bad_case"]})
    return {"purchase_amount": float(purchase_amount), "options": options,
            "forecast_status": forecast["forecast_status"], "uncertain_items": forecast["uncertain_items"]}
