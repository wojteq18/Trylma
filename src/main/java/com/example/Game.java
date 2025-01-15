package com.example;

import java.util.List;
import java.util.Scanner;

import com.example.MoveFactory.MoveHandler;
import com.example.MoveFactory.MoveHandlerFactory;
import com.example.strategy.BoardStrategy;
import com.example.strategy.BoardStrategyFactory;

/**
 * The Game class manages the main flow of a game session, including player interactions,
 * board setup, and turn-based gameplay logic.
 */
public class Game {

    /**
     * The current command received from the input.
     */
    String command;

    /**
     * The action parsed from the command.
     */
    String action;

    /**
     * Coordinates for the current and new positions during a move action.
     */
    int x, y, newX, newY;

    /**
     * Constructs a new Game instance with the specified parameters.
     * Initializes the board using a strategy, creates players, and manages game flow.
     *
     * @param length           the length of the game board
     * @param numberOfPlayers  the number of players in the game
     * @param strategy         the strategy ID used to create the board
     */
    public Game(int length, int numberOfPlayers, int strategy) {
        BoardStrategy boardStrategy = BoardStrategyFactory.getStrategy(strategy);
        Board board = boardStrategy.createBoard(length, numberOfPlayers);

        // Print initial coordinates to the server
        board.printAllCoordinates();
        System.out.flush();

        PlayerManager manager = new PlayerManager(numberOfPlayers, board);
        Player[] players = manager.getPlayers();

        Scanner scanner = new Scanner(System.in); // Read input from the server
        int queue = 0;

        // Main game loop
        while (manager.activePlayers(players) > 1) {
            try {
                command = scanner.nextLine();
                Scanner commandScanner = new Scanner(command);
                action = commandScanner.next();

                if (players[queue].getState() == State.ACTIVE) {

                    if (action.equals("move")) {
                        x = commandScanner.nextInt();
                        y = commandScanner.nextInt();
                        newX = commandScanner.nextInt();
                        newY = commandScanner.nextInt();

                        try {
                            int moveResult = players[queue].move(x, y, newX, newY);
                            MoveHandler handler = MoveHandlerFactory.getHandler(moveResult);
                            handler.handle();

                            if (handler.shouldSwitchPlayer()) {
                                queue = (queue + 1) % numberOfPlayers;
                            }

                        } catch (Exception e) {
                            System.err.println("Error during move execution: " + e.getMessage());
                            e.printStackTrace();
                            System.out.println("error"); // Send error to the server
                            System.out.flush();
                        }

                    } else if (action.equals("wait")) {
                        System.out.println("ok"); // Send response to the server
                        System.out.flush();
                        queue = (queue + 1) % numberOfPlayers;

                    } else if (action.equals("show")) {
                        List<Pawn> get = players[queue].getpawns();
                        StringBuilder boardState = new StringBuilder("Pawns: ");
                        for (Pawn pawn : get) {
                            boardState.append(pawn.getX())
                                    .append(" ")
                                    .append(pawn.getY())
                                    .append(" ")
                                    .append(pawn.getColor())
                                    .append(", ");
                        }
                        if (boardState.length() > 8) {
                            boardState.setLength(boardState.length() - 2);
                        }
                        System.out.println(boardState.toString());
                        System.out.flush();

                    } else if (action.equals("update")) {
                        board.printAllCoordinates();
                        System.out.flush();

                    } else {
                        System.out.println("error"); // Invalid command
                        System.out.flush();
                    }
                    commandScanner.close();

                    // Check if the player has won
                    try {
                        players[queue].hasWon();
                    } catch (Exception e) {
                        System.err.println("Error checking victory: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error during player turn " + (queue + 1) + ": " + e.getMessage());
                e.printStackTrace();
                System.out.println("error");
            }
        }

        // Game over
        try {
            // Uncomment if you wish to print game-over message to the server
            // System.out.println("Game over!");
        } catch (Exception e) {
            System.err.println("Error during game termination: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();
    }
}
