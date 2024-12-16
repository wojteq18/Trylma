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
        //System.out.println("");


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
                   
                    command = scanner.nextLine();                    
                    Scanner commandScanner = new Scanner(command);                    
                    action = commandScanner.next();
                    //System.out.println("Action: " + action);
                    //System.out.flush();

                    
                    if (action.equals("move")) {       
                        //System.out.println("obiad");
                        //System.out.flush();                 
                        x = commandScanner.nextInt();
                        y = commandScanner.nextInt();
                        newX = commandScanner.nextInt();
                        newY = commandScanner.nextInt();
                        // Wykonaj ruch
                        try {
                            int moveResult = players[queue].move(x, y, newX, newY);

                            if (moveResult == 0) {
                                System.out.println("ok"); // Wyślij odpowiedź do serwera
                                System.out.flush();
                                queue = (queue + 1) % numberOfPlayers; // Przejdź do następnego gracza
                            } else if (moveResult == 1) {
                                System.out.println("error: Forbidden move!");
                                System.out.flush();
                            } else if (moveResult == 2) {
                                System.out.println("error: Pawn is trying to escape from final area!");
                                System.out.flush();
                            } else if (moveResult == 3) {
                                System.out.println("error: Final square is not empty!");
                                System.out.flush();
                            } else if (moveResult == 4) {
                                System.out.println("error: Final square does not exist!");
                                System.out.flush();
                            } else if (moveResult == 5) {
                                System.out.println("error: There is no pawn there!");
                                System.out.flush();
                            } else {
                                System.out.println("error: Unknown error occurred.");
                                System.out.flush();
                            }

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
                //System.out.println("ok");
            } catch (Exception e) {
                //System.out.println("ok");
                System.err.println("Błąd w obsłudze tury gracza " + (queue + 1) + ": " + e.getMessage());
                e.printStackTrace();
                System.out.println("error");
            }

            //System.out.println("ok");

            // Wyświetlenie informacji o następnym graczu po zmianie kolejki
            //System.out.println("Kolej gracza: " + (queue + 1));
            //scanner.close();
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
