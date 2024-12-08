package com.example;

import java.util.List;
import java.util.Scanner;

public class Game {
    String command, action;
    int x, y, newX, newY;

    public Game(int length, int numberOfPlayers) {
        FilledBoard board = new FilledBoard(length, numberOfPlayers);
        PlayerManager manager = new PlayerManager(numberOfPlayers, board);
        Player[] players = manager.getPlayers();

        Scanner scanner = new Scanner(System.in); // Odbieraj dane z serwera
        int queue = 0;

        while (manager.activePlayers(players) > 1) {
            // System.out.println("ok");
            try {
                // System.out.println("ok");
                if (players[queue].getState() == State.ACTIVE) {
                    //System.out.println("ok");
                    // Tworzenie reprezentacji stanu planszy
                    List<Pawn> get = players[queue].getpawns();
                    //StringBuilder boardState = new StringBuilder("Pionki: ");
                    /*for (Pawn pawn : get) {
                        boardState.append(pawn.getX())
                                .append(" ")
                                .append(pawn.getY())
                                .append(" ")
                                .append(pawn.getColor())
                                .append(", ");
                    }*/
                    // Usuwamy ostatni przecinek i spację
                    //if (boardState.length() > 8) {
                        //boardState.setLength(boardState.length() - 2);
                    //}
                    // Wyślij cały stan do serwera
                    //System.out.println(boardState.toString());

                    // Oczekiwanie na ruch gracza
                    System.out.println("");
                   
                    command = scanner.nextLine();                    
                    Scanner commandScanner = new Scanner(command);                    
                    action = commandScanner.next();
                    
                    if (action.equals("move") && commandScanner.hasNextInt()) {                        
                        x = commandScanner.nextInt();
                        y = commandScanner.nextInt();
                        newX = commandScanner.nextInt();
                        newY = commandScanner.nextInt();
                        // Wykonaj ruch
                        try {
                            players[queue].move(x, y, newX, newY);
                            System.out.println("ok"); // Wyślij odpowiedź do serwera
                            System.out.flush();
                            // Przejdź do następnego gracza
                            queue = (queue + 1) % numberOfPlayers;
                        } catch (Exception e) {
                            System.err.println("Błąd podczas wykonywania ruchu: " + e.getMessage());
                            e.printStackTrace();
                            System.out.println("error"); // Wyślij błąd do serwera
                            System.out.flush();
                        }
                    } else if (action.equals("wait")) {
                        //System.out.println("2");
                        //System.exit(0); // Kończy program
                        System.out.println("ok"); // Wyślij odpowiedź do serwera
                        System.out.flush();
                        // Przejdź do następnego gracza
                        queue = (queue + 1) % numberOfPlayers;
                        
                    } else {
                        System.out.println("error"); // Błędne polecenie
                        System.out.flush();
                        //System.out.println("3");
                        //System.exit(0); // Kończy program
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
                System.out.println("ok");
            } catch (Exception e) {
                System.out.println("ok");
                System.err.println("Błąd w obsłudze tury gracza " + (queue + 1) + ": " + e.getMessage());
                e.printStackTrace();
                System.out.println("error");
            }

            System.out.println("ok");

            // Wyświetlenie informacji o następnym graczu po zmianie kolejki
            //System.out.println("Kolej gracza: " + (queue + 1));
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
