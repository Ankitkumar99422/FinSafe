import re
from pathlib import Path

import joblib

MODEL_PATH = Path(__file__).with_name("intent_model.joblib")   # created by train_intent.py
CONFIDENCE_THRESHOLD = 0.50     # design choice: below this we do not trust the prediction


def clean_text(text):
    """Lowercase and replace every amount with the word 'num', so '80,000' and '5k' look the same."""
    text = text.lower()
    text = re.sub(r"\d[\d,\.]*\s*(k|lakhs?|lacs?)?\b", " num ", text)
    return re.sub(r"\s+", " ", text).strip()


def extract_amount(text):
    """Pull a rupee amount out of a question. Handles 80,000 / 80000 / 25k / 1.5 lakh. None if absent."""
    m = re.search(r"(\d[\d,]*\.?\d*)\s*(k|lakhs?|lacs?)?\b", text.lower())
    if not m:
        return None
    value = float(m.group(1).replace(",", ""))
    unit = m.group(2)
    if unit == "k":
        value *= 1_000
    elif unit:
        value *= 100_000
    return value


_model = None


def predict_intent(text):
    """Return (intent, confidence). Low confidence becomes 'unknown' so the agent can ask again."""
    global _model
    if _model is None:
        _model = joblib.load(MODEL_PATH)
    probs = _model.predict_proba([text])[0]
    best = probs.argmax()
    confidence = float(probs[best])
    if confidence < CONFIDENCE_THRESHOLD:
        return "unknown", round(confidence, 2)
    return str(_model.classes_[best]), round(confidence, 2)
