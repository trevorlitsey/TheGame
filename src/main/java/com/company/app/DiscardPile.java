package com.company.app;

import java.util.ArrayList;

public class DiscardPile {
    ArrayList<Integer> cards = new ArrayList<>();

    Direction direction;

    DiscardPile(Direction direction) {
        if (direction == Direction.UP) {
            cards.add(1);
        } else if (direction == Direction.DOWN) {
            cards.add(100);
        }

        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getTopCard() {
        return cards.get(cards.size() - 1);
    }

    public void discard(int card) {
        cards.add(card);
    }

    public String toString() {
        return cards.toString();
    }
}
