package com.company.app;

public class CardReport {
    int closestTo;
    int cardIndex;
    int howClose;
    boolean isReverse;

    CardReport(int closestTo, int cardIndex, int howClose, boolean isReverse) {
        this.closestTo = closestTo;
        this.cardIndex = cardIndex;
        this.howClose = howClose;
        this.isReverse = isReverse;
    }

    public int getClosestTo() {
        return closestTo;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public boolean getIsReverse() {
        return isReverse;
    }

    public int getHowClose() {
        return howClose;
    }

    public String toString() {
        return "closestTo: " + closestTo + " index: " + cardIndex + " howClose: " + howClose;
    }

}
