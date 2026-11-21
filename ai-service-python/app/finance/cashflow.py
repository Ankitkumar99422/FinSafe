import pandas as pd
from app.finance.recurring import get_events, expected_next
from app.finance.forecast_config import BAD_CASE_QUANTILE, MIN_HISTORY_DAYS


def _upcoming(events, today, horizon_days, amount_rule):
    nxt = expected_next(events, today, amount_rule)
    end = pd.Timestamp(today) + pd.Timedelta(days=horizon_days)
    d = pd.Timestamp(nxt["expected_date"])
    count = 0
    while d <= end:
        count += 1
        d = (d + pd.offsets.MonthBegin(1)).replace(day=d.day)
    return nxt, count


def _irregular_net(transactions, today, horizon_days):
    rest = transactions[~transactions["category"].isin(["Salary", "Rent"])].copy()
    rest["net"] = rest["deposit"] - rest["withdrawal"]
    days = pd.date_range(transactions["transaction_date"].min(), today, freq="D")
    daily = rest.groupby("transaction_date")["net"].sum().reindex(days).fillna(0)
    sums = daily.rolling(horizon_days).sum().dropna()
    return float(sums.median()), float(sums.quantile(BAD_CASE_QUANTILE))


def forecast_cashflow(transactions, current_balance, today, horizon_days=30):
    today = pd.Timestamp(today)
    tx = transactions[transactions["transaction_date"] <= today]
    if (today - tx["transaction_date"].min()).days < MIN_HISTORY_DAYS:
        raise ValueError("Not enough history to forecast")
    income, fixed = 0.0, 0.0
    uncertain = []
    sal_nxt, sal_n = _upcoming(get_events(tx, "Salary", "deposit"), today, horizon_days, "min")
    if sal_nxt["status"] == "overdue":
        uncertain.append({"item": "Salary", "reason": "overdue", "treatment": "excluded",
                          "amount": sal_nxt["expected_amount"] * max(sal_n, 1)})
    else:
        income = sal_nxt["expected_amount"] * sal_n
    rent_nxt, rent_n = _upcoming(get_events(tx, "Rent", "withdrawal"), today, horizon_days, "max")
    fixed = rent_nxt["expected_amount"] * rent_n
    if rent_nxt["status"] == "overdue":
        uncertain.append({"item": "Rent", "reason": "overdue", "treatment": "included", "amount": fixed})
    typical, bad = _irregular_net(tx, today, horizon_days)
    return {
        "horizon_days": horizon_days, "current_balance": float(current_balance),
        "expected_income": income, "expected_fixed_expenses": fixed,
        "irregular_net_typical": round(typical, 2), "irregular_net_bad_case": round(bad, 2),
        "projected_balance_typical": round(current_balance + income - fixed + typical, 2),
        "projected_balance_bad_case": round(current_balance + income - fixed + bad, 2),
        "forecast_status": "uncertain" if uncertain else "normal", "uncertain_items": uncertain,
    }
