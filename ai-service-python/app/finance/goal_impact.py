import math

import pandas as pd


def calculate_goal_impact(goal, forecast, purchase_amount, today):
    today = pd.Timestamp(today)
    days_left = (pd.Timestamp(goal["deadline"]) - today).days
    remaining = max(0.0, goal["target_amount"] - goal["current_amount"])
    result = {"goal_name": goal["goal_name"], "priority": goal["priority"],
              "remaining_need": round(remaining, 2)}
    if remaining == 0:
        return {**result, "status": "goal_reached"}
    if days_left <= 0:
        return {**result, "status": "deadline_passed"}
    months_left = days_left / 30
    required_before = remaining / months_left
    required_after = (remaining + purchase_amount) / months_left
    capacity = max(0.0, forecast["projected_balance_typical"] - forecast["current_balance"])
    if required_before > capacity:
        status = "already_off_track"
    elif required_after > capacity:
        status = "delayed_by_purchase"
    else:
        status = "on_track_after_purchase"
    if status == "on_track_after_purchase":
        extension_days = 0
    elif capacity > 0:
        months_needed = (remaining + purchase_amount) / capacity
        extension_days = max(0, math.ceil(months_needed * 30) - days_left)
    else:
        extension_days = None
    return {**result, "months_left": round(months_left, 2),
            "required_monthly_before": round(required_before, 2),
            "required_monthly_after": round(required_after, 2),
            "extra_monthly_needed": round(required_after - required_before, 2),
            "monthly_capacity": round(capacity, 2),
            "extension_days_needed": extension_days, "status": status}
