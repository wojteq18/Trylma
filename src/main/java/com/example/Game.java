package com.example;

import java.util.List;
import java.util.Scanner;

import com.example.MoveFactory.MoveHandler;
import com.example.MoveFactory.MoveHandlerFactory;

public class Game {
    String command, action;
    int x, y, newX, newY;

    public Game(int length, int numberOfPlayers, int strategy) { //TODO - dodac strategie
        Board board;
        switch (strategy) {
            case 1:
                board = new FilledBoard(length, numberOfPlayers);
                break;
            case 2:
                board = new YinBoard(length, numberOfPlayers);
                break;
            case 3:
                board = new ChaosBoard(length, numberOfPlayers);
                break;
            default:
                throw new IllegalArgumentException("Invalid strategy: " + strategy);
        }
        
        //do serwera wysyłamy wszystkie koordynaty na start
        board.printAllCoordinates();
        System.out.flush();

        PlayerManager manager = new PlayerManager(numberOfPlayers, board);
        Player[] players = manager.getPlayers();

        Scanner scanner = new Scanner(System.in); // Odbieraj dane z serwera
        int queue = 0;

        while (manager.activePlayers(players) > 1) {
            try {
                if (players[queue].getState() == State.ACTIVE) {

                    // Oczekiwanie na ruch gracza
                    command = scanner.nextLine();                    
                    Scanner commandScanner = new Scanner(command);                    
                    action = commandScanner.next();
         
                    if (action.equals("move")) {                        
                        x = commandScanner.nextInt();
                        y = commandScanner.nextInt();
                        newX = commandScanner.nextInt();
                        newY = commandScanner.nextInt();
                        // Wykonaj ruch
                        try {
                            int moveResult = players[queue].move(x, y, newX, newY);

                            MoveHandler handler = MoveHandlerFactory.getHandler(moveResult); //obsluga ruchu, fabryka massege
                            handler.handle();

                            if (handler.shouldSwitchPlayer()) {
                                queue = (queue + 1) % numberOfPlayers;
                            }

                        } catch (Exception e) {
                            System.err.println("Błąd podczas wykonywania ruchu: " + e.getMessage());
                            e.printStackTrace();
                            System.out.println("error"); // Wyślij błąd do serwera
                            System.out.flush();
                        }
                    } else if (action.equals("wait")) {
                        System.out.println("ok"); // Wyślij odpowiedź do serwera
                        System.out.flush();

                        // Przejdź do następnego gracza
                        queue = (queue + 1) % numberOfPlayers;
                        
                    } else if (action.equals("show")) {
                        // Tworzenie reprezentacji stanu planszy
                        List<Pawn> get = players[queue].getpawns();
                        StringBuilder boardState = new StringBuilder("Pionki: ");
                        for (Pawn pawn : get) {
                            boardState.append(pawn.getX())
                                    .append(" ")
                                    .append(pawn.getY())
                                    .append(" ")
                                    .append(pawn.getColor())
                                    .append(", ");
                        }
                        // Usuwamy ostatni przecinek i spację
                        if (boardState.length() > 8) {
                            boardState.setLength(boardState.length() - 2);
                        }
                        // Wyślij cały stan do serwera
                        System.out.println(boardState.toString());
                        System.out.flush(); 
                        }
                    else if (action.equals("update")) {
                        board.printAllCoordinates();
                        System.out.flush();
                    }    
                    else {
                        System.out.println("error"); // Błędne polecenie
                        System.out.flush();
                    }
                    commandScanner.close();

                    // Sprawdź, czy gracz wygrał
                    try {
                        players[queue].hasWon();
                    } catch (Exception e) {
                        System.err.println("Błąd podczas sprawdzania wygranej: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                //System.out.println("ok");
            } catch (Exception e) {
                //System.out.println("ok");
                System.err.println("Błąd w obsłudze tury gracza " + (queue + 1) + ": " + e.getMessage());
                e.printStackTrace();
                System.out.println("error");
            }
        }

        // Zakończenie gry
        try {
            //System.out.println("Game over!");
        } catch (Exception e) {
            System.err.println("Błąd podczas zakończenia gry: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();
    }
}
