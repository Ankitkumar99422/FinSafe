from pathlib import Path

import joblib
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline

from app.model.intent_model import clean_text, MODEL_PATH

df = pd.read_csv(Path(__file__).with_name("intent_data.csv"))
X_train, X_test, y_train, y_test = train_test_split(
    df["text"], df["intent"], test_size=0.2, stratify=df["intent"], random_state=42)

model = Pipeline([
    ("tfidf", TfidfVectorizer(preprocessor=clean_text, ngram_range=(1, 2))),
    ("clf", LogisticRegression(C=10, max_iter=1000)),
])
model.fit(X_train, y_train)

print("Train questions:", len(X_train), "| Test questions:", len(X_test))
print(classification_report(y_test, model.predict(X_test)))

model.fit(df["text"], df["intent"])          # final model learns from everything
joblib.dump(model, MODEL_PATH)
print("Saved", MODEL_PATH)
