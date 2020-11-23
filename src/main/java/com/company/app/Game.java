package com.company.app;

import java.util.*;

public class Game {

    private ArrayList<DiscardPile> discardPiles = new ArrayList<>(4);

    private ArrayList<Integer> deck;

    private ArrayList<Integer> hand = new ArrayList<>(0);

    private Outcome outcome;

    public Game() {
        prepareDeck();
        initDiscardPiles();
        initHand();
    }

    public Result playGame() {
        while (outcome != Outcome.LOSE) {
            drawCards();

            boolean didPlayFirstCard = playCard();

            if (!didPlayFirstCard) {
                outcome = Outcome.LOSE;
                break;
            }

            if (deck.size() > 0) {
                // play two cards while deck still has cards in it
                boolean didPlaySecondCard = playCard();

                if (!didPlaySecondCard) {
                    outcome = Outcome.LOSE;
                    break;
                }
            }

            if (hand.size() <= 6 && deck.size() > 0) {
                drawCards();
            }

            if (hand.size() == 0 && deck.size() == 0) {
                // win!
                outcome = Outcome.WIN;
                break;
            }
        }

        int cardsLeft = hand.size() + deck.size();
        return new Result(outcome, cardsLeft);
    }

    private void prepareDeck() {
        ArrayList<Integer> oneThroughNinetyNine = new ArrayList<>(98);
        for (int i = 2; i < 100; i++) {
            oneThroughNinetyNine.add((i));
        }
        Collections.shuffle(oneThroughNinetyNine);
        deck = oneThroughNinetyNine;
    }

    private void initDiscardPiles() {
        for (int i = 0; i < 2; i++) {
            discardPiles.add(new DiscardPile(Direction.UP));
        }

        for (int i = 0; i < 2; i++) {
            discardPiles.add(new DiscardPile(Direction.DOWN));
        }
    }

    private void initHand() {
        hand = new ArrayList<>();
    }

    private void drawCards() {
        while (deck.size() > 0 && hand.size() < 8) {
            hand.add(deck.remove(deck.size() - 1));
        }
    }

    private boolean playCard() {
        CardReport bestPlayPossible = findBestPlayPossible();

        if (bestPlayPossible == null) {
            return false;
        } else {
            // play card into pile
            Integer cardToPlay = hand.remove(bestPlayPossible.getCardIndex());
            discardPiles.get(bestPlayPossible.getClosestTo()).discard(cardToPlay);
            return true;
        }
    }

    private CardReport findBestPlayPossible() {
        // sort hand
        hand.sort(Comparator.comparingInt(a -> a));

        // generate reports for top card of each discard pile
        List<CardReport> cardReportsOfClosestCardsToEachPile = new ArrayList<>();
        for (int i = 0; i < discardPiles.size(); i++) {
            cardReportsOfClosestCardsToEachPile.add(getCardReportOfDiscardIndexPile(i));
        }

        // determine the smallest addition possible
        CardReport bestPlayPossible = null;
        for (CardReport report : cardReportsOfClosestCardsToEachPile) {
            boolean noPlayFoundYetAndReportIsValid = bestPlayPossible == null && report.getHowClose() != -1;
            boolean smallerPlayFound = bestPlayPossible != null && bestPlayPossible.getHowClose() != -10 && report.getHowClose() != -1 && report.getHowClose() < bestPlayPossible.getHowClose();
            boolean reversePlayFound = report.getIsReverse();

            if (noPlayFoundYetAndReportIsValid || smallerPlayFound || reversePlayFound) {
                bestPlayPossible = report;

                if (reversePlayFound) {
                    break;
                }
            }
        }

       return bestPlayPossible;
    }

    private CardReport getCardReportOfDiscardIndexPile(int indexOfDiscardPile) {
        DiscardPile discardPile = discardPiles.get(indexOfDiscardPile);
        Integer cardToGetCloseTo = discardPile.getTopCard();
        Direction direction = discardPile.getDirection();

        int indexOfClosestCardFound = -1;
        int howClose = -1;
        boolean isReverse = false;

        // left to consider
        // playing in-between the current card and one that can go backwards
        // total cards playable in a cluster?

        for (int i = 0; i < hand.size(); i++) {
            Integer card = hand.get(i);
            if (direction == Direction.UP) {
                if (card == cardToGetCloseTo - 10) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                    isReverse = true;
                    break;
                } else if (card > cardToGetCloseTo && (indexOfClosestCardFound == -1 || card < indexOfClosestCardFound)) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                }
            } else if (direction == Direction.DOWN) {
                if (card == cardToGetCloseTo + 10) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                    isReverse = true;
                    break;
                } else if (card < cardToGetCloseTo && (indexOfClosestCardFound == -1 || card > indexOfClosestCardFound)) {
                    indexOfClosestCardFound = i;
                    howClose = cardToGetCloseTo - hand.get(i);
                }
            }
        }

        return new CardReport(indexOfDiscardPile, indexOfClosestCardFound, howClose, isReverse);
    }
}
