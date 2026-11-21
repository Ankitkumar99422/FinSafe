def validate(candidate):
    if candidate["forecast_status"] == "uncertain" and candidate["recommendation"] in ("Buy Now", "EMI"):
        return {"status": "MODIFY", "recommendation": "Wait",
                "reasons": ["Forecast is uncertain, so a purchase was not approved (example rule)"]}
    return {"status": "PASS", "recommendation": candidate["recommendation"], "reasons": []}
