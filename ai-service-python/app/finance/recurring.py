import pandas as pd

from app.finance.forecast_config import GRACE_DAYS, N_RECENT


def get_events(transactions, category, amount_col):
    ev = transactions[(transactions["category"] == category) & (transactions[amount_col] > 0)]
    ev = ev.sort_values("transaction_date")
    return ev[["transaction_date", amount_col]].rename(columns={amount_col: "amount"})


def expected_next(events, today, amount_rule="min"):
    today = pd.Timestamp(today)
    last_date = events["transaction_date"].iloc[-1]
    day = min(int(events["transaction_date"].dt.day.median()), 28)
    next_date = (last_date + pd.offsets.MonthBegin(1)).replace(day=day)
    recent = events["amount"].tail(N_RECENT)
    amount = float(recent.min() if amount_rule == "min" else recent.max())
    status = "overdue" if today > next_date + pd.Timedelta(days=GRACE_DAYS) else "expected"
    return {"expected_date": next_date.date(), "expected_amount": amount, "status": status}
