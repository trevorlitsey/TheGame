package com.company.app;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int numberOfGamesToPlay = 100000;
        List<Result> results = new ArrayList<>();

        for (int i = 0; i < numberOfGamesToPlay; i++) {
            results.add(new Game().playGame());
        }

        List<Result> gamesLost = results.stream().filter(result -> result.getOutcome() == Outcome.LOSE).collect(Collectors.toList());

        int numberOfGamesWon = numberOfGamesToPlay - gamesLost.size();
        Double averageNumberOfCardsLeft = gamesLost.stream().collect(Collectors.averagingInt(Result::getCardsLeft));
        System.out.println("Games Played: " + numberOfGamesToPlay);
        System.out.println("Games Won: " + numberOfGamesWon + " (" + ((float)numberOfGamesWon / numberOfGamesToPlay * 100) + "%)");
        System.out.println("Games Lost: " + gamesLost.size() + " (" + ((float)gamesLost.size() / numberOfGamesToPlay * 100) + "%)");
        System.out.println("Average Cards Left When Lost: " + averageNumberOfCardsLeft);
    }
}
