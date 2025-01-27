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
    public Game(int length, int numberOfPlayers, int strategy, String savedString, boolean isBot) {
        Board board;
        if (strategy != 4) {
            BoardStrategy boardStrategy = BoardStrategyFactory.getStrategy(strategy);
            board = boardStrategy.createBoard(length, numberOfPlayers);
            }
        else {
            board = new BoardLoader(length, numberOfPlayers, savedString);
        }    

        // Print initial coordinates to the server
        board.printAllCoordinates();
        System.out.flush();

        PlayerManager manager = new PlayerManager(numberOfPlayers, board, isBot);
        Player[] players = manager.getPlayers();

        Scanner scanner = new Scanner(System.in); // Read input from the server
        int queue = 0;

        // Main game loop
// ...
        while (manager.activePlayers(players) > 1) {
            try {
                // Odczyt komendy od serwera (zwykle tylko w turze człowieka)
                command = scanner.nextLine();
                Scanner commandScanner = new Scanner(command);
                action = commandScanner.next();

                // Jeśli gracz w kolejce jest aktywny...
                if (players[queue].getState() == State.ACTIVE) {
                    players[queue].addMoves();

                    if (!players[queue].isBot()) {
                        
                        if (action.equals("move")) {
            
                            x = commandScanner.nextInt();
                            y = commandScanner.nextInt();
                            newX = commandScanner.nextInt();
                            newY = commandScanner.nextInt();

                            try {
                                int moveResult = players[queue].move(x, y, newX, newY);
                                MoveHandler handler = MoveHandlerFactory.getHandler(moveResult);
                                handler.handle();

                                // Jeśli ruch udany i wymaga zmiany kolejki
                                if (handler.shouldSwitchPlayer()) {
                                    queue = (queue + 1) % numberOfPlayers;
                                }

                            } catch (Exception e) {
                                System.err.println("Error during move execution: " + e.getMessage());
                                e.printStackTrace();
                                System.out.println("error");
                                System.out.flush();
                            }

                        } else if (action.equals("wait")) {
                            // Gracz decyduje się oddać ruch
                            System.out.println("ok");
                            System.out.flush();
                            queue = (queue + 1) % numberOfPlayers;

                        } else if (action.equals("show")) {
                            // Wyświetlamy pionki aktualnego gracza
                            List<Pawn> get = players[queue].getpawns();
                            StringBuilder boardState = new StringBuilder("Pionki: ");
                            for (Pawn pawn : get) {
                                boardState.append("(")
                                        .append(pawn.getX()).append(", ")
                                        .append(pawn.getY()).append(", ")
                                        .append(pawn.getColor()).append("), ");
                            }
                            if (boardState.length() > 8) {
                                boardState.setLength(boardState.length() - 2);
                            }
                            System.out.println(boardState.toString());
                            System.out.flush();

                        } else if (action.equals("update")) {
                            // Gracz prosi o aktualny stan pionków
                            board.printAllCoordinates();
                            System.out.flush();

                        } else {
                            // Nierozpoznana komenda
                            System.out.println("error");
                            System.out.flush();
                        }
                        commandScanner.close();
                    } 
                    // 2. Tura bota
                    else {
                        int[] bestCoords = players[queue].bestMove();
                            // Bot lokalnie wykonuje ruch
                        players[queue].move(bestCoords[0], bestCoords[1], 
                                            bestCoords[2], bestCoords[3]);
                            board.printAllCoordinates();
                            System.out.flush();


                        queue = (queue + 1) % numberOfPlayers;
                    }

                    // Sprawdzamy, czy gracze nie wygrali
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
// ...


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

    private boolean isLastActiveBot(int currentQueue, Player[] players) {
        int numberOfPlayers = players.length;

        // Sprawdzamy kolejnych graczy w kolejce
        for (int i = 1; i < numberOfPlayers; i++) {
            int nextQueue = (currentQueue + i) % numberOfPlayers;
            Player nextPlayer = players[nextQueue];

            if (nextPlayer.getState() == State.ACTIVE && !nextPlayer.isBot()) {
                // Jeśli znajdziemy aktywnego człowieka, obecny bot jest ostatnim
                return true;
            }

            if (nextPlayer.getState() == State.ACTIVE && nextPlayer.isBot()) {
                // Jeśli znajdziemy aktywnego bota, obecny bot nie jest ostatnim
                return false;
            }
        }

        // Jeśli nie znaleziono aktywnego człowieka, obecny bot jest ostatnim
        return true;
    }

}