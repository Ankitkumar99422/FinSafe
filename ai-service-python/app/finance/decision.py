from app.finance.affordability import check_affordability
from app.finance.payment_options import compare_payment_options
from app.finance.goal_impact import calculate_goal_impact
from app.finance.decision_config import EMI_CHOICE_RULE
from app.validator.placeholder_validator import validate


def make_decision(forecast, purchase_amount, emi_offers, goals, today):
    comparison = compare_payment_options(forecast, purchase_amount, emi_offers)
    options = comparison["options"]
    buy_now = options[0]
    if EMI_CHOICE_RULE == "lowest_cost":
        pick_key = lambda o: o["extra_cost"]
    else:                                            # "most_cushion": biggest bad-case balance, then cheapest
        pick_key = lambda o: (-o["balance_after_bad_case"], o["extra_cost"])
    affordable_emis = sorted(
        [o for o in options if o["option"] == "emi" and o["verdict"] == "affordable"],
        key=pick_key,
    )
    if buy_now["verdict"] == "affordable":
        recommendation, chosen = "Buy Now", buy_now
    elif affordable_emis:
        recommendation, chosen = "EMI", affordable_emis[0]
    elif any(o["verdict"] == "risky" for o in options):
        recommendation, chosen = "Wait", None
    else:
        recommendation, chosen = "Not Recommended", None

    affordability = check_affordability(forecast, purchase_amount)
    goal_impacts = [calculate_goal_impact(g, forecast, purchase_amount, today) for g in goals]
    goal_warnings = [g for g in goal_impacts if g["status"] in ("delayed_by_purchase", "already_off_track")]

    forecast_keys = ["current_balance", "expected_income", "expected_fixed_expenses",
                     "projected_balance_typical", "projected_balance_bad_case"]
    candidate = {
        "purchase_amount": float(purchase_amount),
        "forecast": {k: forecast[k] for k in forecast_keys if k in forecast},
        "recommendation": recommendation,
        "chosen_option": chosen,
        "options": options,
        "goal_warnings": goal_warnings,
        "affordability": affordability,
        "max_safe_purchase": affordability["max_safe_purchase"],
        "forecast_status": forecast["forecast_status"],
        "uncertain_items": forecast["uncertain_items"],
    }
    validation = validate(candidate)
    return {"candidate": candidate, "validation": validation,
            "final_recommendation": validation["recommendation"]}
