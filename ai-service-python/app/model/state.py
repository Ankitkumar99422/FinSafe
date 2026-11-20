from typing import TypedDict, Optional


class AgentState(TypedDict):
    """The shared notebook. Every node reads from it and writes only what it changes."""
    question: str
    today: str
    intent: str
    confidence: float
    amount: Optional[float]
    forecast: Optional[dict]
    decision: Optional[dict]
    answer: str
