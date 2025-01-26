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
        String savedBoard = null;
        Scanner scanner = new Scanner(System.in);

        // Read the number of players from the server
        int maxPlayers = scanner.nextInt();

        // Read the strategy ID from the server
        int strategy = scanner.nextInt();

        if (strategy == 4 ) {
            savedBoard = "(13, -3, BLACK), (11, -1, BLACK), (13, 1, BLACK), (13, 3, BLACK), (14, -2, BLACK), (14, 0, BLACK), (14, 2, BLACK), (15, -1, BLACK), (15, 1, BLACK), (16, 0, BLACK), (0, 0, WHITE), (1, -1, WHITE), (1, 1, WHITE), (2, -2, WHITE), (2, 0, WHITE), (2, 2, WHITE), (3, -3, WHITE), (4, 0, WHITE), (3, 1, WHITE), (3, 3, WHITE)";
        }

        // Create and start the game with the specified board size, number of players, and strategy
        new Game(5, maxPlayers, strategy, savedBoard);

        scanner.close();
    }
}
