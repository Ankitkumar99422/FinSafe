from langgraph.graph import StateGraph, START, END

from app.model.state import AgentState
from app.explanation import templates
from app.model.intent_model import predict_intent, extract_amount
from app.finance.data_tools import get_balance, get_upcoming_expenses
from app.finance.cashflow import forecast_cashflow
from app.finance.decision import make_decision
from app.finance.goal_impact import calculate_goal_impact

NEEDS_AMOUNT = {"check_purchase", "compare_payment"}


def build_agent(transactions, goals, emi_offers):
    """Build the agent. The data and settings are passed in, as the backend would supply them."""

    def understand(state):
        intent, confidence = predict_intent(state["question"])
        return {"intent": intent, "confidence": confidence, "amount": extract_amount(state["question"])}

    def route_after_understand(state):
        if state["intent"] in ("other", "unknown"):
            return "out_of_scope"
        if state["intent"] in NEEDS_AMOUNT and state["amount"] is None:
            return "ask_amount"
        if state["intent"] == "balance_query":
            return "balance"
        if state["intent"] == "upcoming_expenses":
            return "upcoming"
        return "forecast"

    def out_of_scope(state):
        return {"answer": templates.OUT_OF_SCOPE}

    def ask_amount(state):
        return {"answer": templates.ASK_AMOUNT}

    def balance(state):
        return {"answer": templates.explain_balance(get_balance(transactions, state["today"]), state["today"])}

    def upcoming(state):
        return {"answer": templates.explain_upcoming(get_upcoming_expenses(transactions, state["today"]))}

    def forecast(state):
        try:
            current = get_balance(transactions, state["today"])
            return {"forecast": forecast_cashflow(transactions, current, state["today"])}
        except ValueError:
            return {"forecast": None, "answer": templates.NOT_ENOUGH_HISTORY}

    def route_after_forecast(state):
        if state["forecast"] is None:
            return END
        return "goal" if state["intent"] == "goal_impact" else "decide"

    def decide(state):
        return {"decision": make_decision(state["forecast"], state["amount"], emi_offers, goals, state["today"])}

    def explain(state):
        return {"answer": templates.explain_decision(state["decision"])}

    def goal(state):
        amount = state["amount"] or 0.0          # no amount = just show the goal's own status
        results = [calculate_goal_impact(g, state["forecast"], amount, state["today"]) for g in goals]
        return {"answer": templates.explain_goals(results)}

    g = StateGraph(AgentState)
    for name, fn in [("understand", understand), ("out_of_scope", out_of_scope), ("ask_amount", ask_amount),
                     ("balance", balance), ("upcoming", upcoming), ("forecast", forecast),
                     ("decide", decide), ("explain", explain), ("goal", goal)]:
        g.add_node(name, fn)

    g.add_edge(START, "understand")
    g.add_conditional_edges("understand", route_after_understand,
                            ["out_of_scope", "ask_amount", "balance", "upcoming", "forecast"])
    g.add_conditional_edges("forecast", route_after_forecast, ["goal", "decide", END])
    g.add_edge("decide", "explain")
    for end_node in ["out_of_scope", "ask_amount", "balance", "upcoming", "explain", "goal"]:
        g.add_edge(end_node, END)
    return g.compile()


def ask(app, question, today):
    state = {"question": question, "today": today, "intent": "", "confidence": 0.0,
             "amount": None, "forecast": None, "decision": None, "answer": ""}
    return app.invoke(state)
