package com.company.app;

public class Result {
    Outcome outcome;
    int cardsLeft;

    public Result(Outcome outcome, int cardsLeft) {
        this.outcome = outcome;
        this.cardsLeft = cardsLeft;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public int getCardsLeft() {
        return cardsLeft;
    }
}
