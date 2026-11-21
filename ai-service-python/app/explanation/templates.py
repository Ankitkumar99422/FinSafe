"""Turns tool results into sentences. Every number here comes straight from a tool result."""


def rs(x):
    return f"Rs {x:,.0f}"


def explain_decision(result):
    c, v = result["candidate"], result["validation"]
    final, a = result["final_recommendation"], c["affordability"]
    amount = c["purchase_amount"]
    lines = []

    if final == "Buy Now":
        lines.append(f"Recommendation: BUY NOW. After paying {rs(amount)}, your projected balance would be about "
                     f"{rs(a['balance_after_typical'])} in a typical month and {rs(a['balance_after_bad_case'])} in a bad month.")
    elif final == "EMI":
        o = c["chosen_option"]
        lines.append(f"Recommendation: EMI. Paying {rs(amount)} in full is not safe, but a {o['tenure_months']}-month EMI "
                     f"of {rs(o['monthly_payment'])} fits. You would pay {rs(o['extra_cost'])} extra in total "
                     f"({rs(o['total_cost'])}). It leaves about {rs(o['balance_after_bad_case'])} in a bad month.")
    elif final == "Wait" and v["status"] == "MODIFY":
        lines.append("Recommendation: WAIT. " + " ".join(v["reasons"]))
    elif final == "Wait":
        lines.append(f"Recommendation: WAIT. In a typical month this purchase would fit, but in a bad month your balance "
                     f"would fall {rs(a['shortfall_bad_case'])} below your safety balance.")
    else:
        lines.append(f"Recommendation: NOT RECOMMENDED. Even in a typical month this purchase would leave you "
                     f"{rs(a['shortfall_typical'])} below your safety balance.")

    if final in ("Wait", "Not Recommended"):
        if c["max_safe_purchase"] > 0:
            lines.append(f"A purchase of up to {rs(c['max_safe_purchase'])} is safe right now.")
        else:
            lines.append("Right now no extra purchase is safe.")

    lines.append("Options compared:")
    for o in c["options"]:
        if o["option"] == "buy_now":
            lines.append(f"  - Pay in full {rs(o['total_cost'])}: {o['verdict']}")
        else:
            lines.append(f"  - EMI {o['tenure_months']} months: {rs(o['monthly_payment'])} a month, "
                         f"{rs(o['extra_cost'])} extra, leaves {rs(o['balance_after_bad_case'])} in a bad month: {o['verdict']}")

    for g in c["goal_warnings"]:
        lines.append(explain_goal(g))
    for u in c["uncertain_items"]:
        lines.append(f"Note: expected {u['item']} ({u['reason']}) was {u['treatment']} "
                     f"for {rs(u['amount'])} in this forecast.")
    return "\n".join(lines)


GOAL_WORDS = {
    "on_track_after_purchase": "still on track",
    "delayed_by_purchase": "on track now but would be delayed by this purchase",
    "already_off_track": "already off track",
    "goal_reached": "already reached",
    "deadline_passed": "past its deadline",
}


def explain_goal(g):
    text = f"Goal '{g['goal_name']}' ({g['priority']} priority) is {GOAL_WORDS[g['status']]}."
    if "extra_monthly_needed" in g and g["extra_monthly_needed"] > 0:
        text += f" It would need {rs(g['extra_monthly_needed'])} more saved each month."
    if g.get("extension_days_needed"):
        text += f" The deadline would need to move about {g['extension_days_needed']} days."
    return text


def explain_goals(results):
    if not results:
        return "You have no savings goals set up."
    return "\n".join(explain_goal(g) for g in results)


def explain_balance(balance, today):
    return f"Your balance as of {today} is Rs {balance:,.2f}."


def explain_upcoming(upcoming):
    if not upcoming["items"]:
        return "No known expenses are due in the next 30 days."
    lines = [f"Rent {i['expected_date']}: {rs(i['amount'])} ({i['status']})" for i in upcoming["items"]]
    return "Upcoming expenses:\n" + "\n".join(lines) + f"\nTotal: {rs(upcoming['total'])}"


ASK_AMOUNT = "How much does the item cost? For example: 'Can I buy a laptop for 80,000?'"
OUT_OF_SCOPE = ("I can help with: whether you can afford a purchase, comparing EMI with paying in full, "
                "the effect on your savings goals, your balance, and your upcoming expenses.")
NOT_ENOUGH_HISTORY = "I don't have enough transaction history yet to make a forecast."
