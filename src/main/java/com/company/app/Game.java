package com.company.app;

import java.util.*;

public class Game {

    private ArrayList<DiscardPile> discardPiles = new ArrayList<>(4);

    private ArrayList<Integer> deck;

    private ArrayList<Integer> hand = new ArrayList<>(8);

    private Outcome outcome;

    public Game() {
        initDeck();
        initDiscardPiles();
    }

    public Result playGame() {
        while (outcome != Outcome.LOSE) {
            drawCards();

            boolean didTakeTurn = takeTurn();

            if (!didTakeTurn) {
                outcome = Outcome.LOSE;
                break;
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

    private void initDeck() {
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
            discardPiles.add(new DiscardPile(Direction.DOWN));
        }
    }

    private void drawCards() {
        while (deck.size() > 0 && hand.size() < 8) {
            hand.add(deck.remove(deck.size() - 1));
        }
    }

    private boolean takeTurn() {
        boolean didPlayCard = false;
        for (int i = 0; i < 2; i++) {
            CardReport bestPlayPossible = findBestPlayPossible();

            if (bestPlayPossible == null) {
                outcome = Outcome.LOSE;
            } else {
                didPlayCard = true;
                playCard(bestPlayPossible);
            }

            // only play one card if deck is out
            if (deck.size() == 0) {
                break;
            }
        }

        return didPlayCard;
    }

    private void playCard(CardReport bestPlayPossible) {
        // play card into pile
        Integer cardToPlay = hand.remove(bestPlayPossible.getCardIndex());
        discardPiles.get(bestPlayPossible.getClosestTo()).discard(cardToPlay);
    }

    private CardReport findBestPlayPossible() {
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

        // order of preference
        // 1. can go backwards (in either direction - no preference)
        // 2. small interval possible (in either direction - no preference)

        // left to consider
        // proximity to outer edges?
        // total cards playable in a cluster?

        for (int i = 0; i < hand.size(); i++) {
            Integer card = hand.get(i);
            if (direction == Direction.UP) {
                if (card == cardToGetCloseTo - 10) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                    isReverse = true;
                    break;
                } else if (card > cardToGetCloseTo && (indexOfClosestCardFound == -1 || card < hand.get(indexOfClosestCardFound))) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                }
            } else if (direction == Direction.DOWN) {
                if (card == cardToGetCloseTo + 10) {
                    indexOfClosestCardFound = i;
                    howClose = hand.get(i) - cardToGetCloseTo;
                    isReverse = true;
                    break;
                } else if (card < cardToGetCloseTo && (indexOfClosestCardFound == -1 || card > hand.get(indexOfClosestCardFound))) {
                    indexOfClosestCardFound = i;
                    howClose = cardToGetCloseTo - hand.get(i);
                }
            }
        }

        return new CardReport(indexOfDiscardPile, indexOfClosestCardFound, howClose, isReverse);
    }
}
