import pandas as pd
from app.finance.recurring import get_events, expected_next
from app.finance.forecast_config import GRACE_DAYS


def get_balance(transactions, today):
    """Balance as of the end of `today`: the last known balance on or before that day."""
    known = transactions[transactions["transaction_date"] <= pd.Timestamp(today)]
    if known.empty:
        raise ValueError("No transactions on or before today")
    return float(known["balance"].iloc[-1])


def get_upcoming_expenses(transactions, today, horizon_days=30):
    """Known recurring expenses (rent) expected within the horizon."""
    today = pd.Timestamp(today)
    tx = transactions[transactions["transaction_date"] <= today]
    nxt = expected_next(get_events(tx, "Rent", "withdrawal"), today, "max")
    end = today + pd.Timedelta(days=horizon_days)

    items = []
    d = pd.Timestamp(nxt["expected_date"])
    while d <= end:
        items.append({"item": "Rent", "expected_date": str(d.date()),
                      "amount": nxt["expected_amount"],
                      "status": "overdue" if today > d + pd.Timedelta(days=GRACE_DAYS) else "expected"})
        d = (d + pd.offsets.MonthBegin(1)).replace(day=d.day)
    return {"items": items, "total": sum(i["amount"] for i in items)}
