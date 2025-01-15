package com.example;

import java.util.Scanner;

/**
 * The Main class serves as the entry point for the application.
 * It initializes the game by reading input parameters for the number of players and strategy.
 */
public class Main {

    /**
     * The main method reads game configuration from input, initializes the game, and starts it.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the number of players from the server
        int maxPlayers = scanner.nextInt();

        // Read the strategy ID from the server
        int strategy = scanner.nextInt();

        // Create and start the game with the specified board size, number of players, and strategy
        new Game(5, maxPlayers, strategy);

        scanner.close();
    }
}
