package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GameLogic {

    private static int currentPlayer = 0;
    private static boolean expectEvenNumbers = true; // Domyślnie oczekujemy liczb parzystych

    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(System.out);

        try {
            // Odbieranie liczby graczy od serwera na początku
            String playerCountStr = input.readLine();
            if (playerCountStr == null || playerCountStr.isEmpty()) {
                throw new IOException("Nie podano liczby graczy");
            }

            int playerCount = Integer.parseInt(playerCountStr.trim());
            expectEvenNumbers = (playerCount % 2 == 0); // Parzyste liczby graczy -> liczby parzyste, inaczej nieparzyste
            output.println("ok: Oczekiwane liczby " + (expectEvenNumbers ? "parzyste" : "nieparzyste"));
            output.flush();

            // Rozpoczęcie obsługi graczy
            while (true) {
                try {
                    // Oczekiwanie na wejście od klienta
                    String line = input.readLine();
                    if (line == null || line.isEmpty()) {
                        output.println("error: Nie podano liczby");
                        output.flush();
                        continue;
                    }

                    // Sprawdzenie, czy wejście jest liczbą
                    try {
                        int number = Integer.parseInt(line.trim());
                        boolean isEven = (number % 2 == 0);

                        if (isEven != expectEvenNumbers) {
                            output.println("error: Liczba musi być " + (expectEvenNumbers ? "parzysta" : "nieparzysta"));
                            output.flush();
                            continue;
                        }

                        // Wszystko w porządku
                        output.println("ok: Liczba zaakceptowana od gracza " + (currentPlayer + 1));
                        output.flush();

                        // Przejście do następnego gracza
                        currentPlayer = (currentPlayer + 1) % playerCount;
                    } catch (NumberFormatException e) {
                        output.println("error: Niepoprawny format, oczekiwano liczby");
                        output.flush();
                    }
                } catch (IOException e) {
                    output.println("error: Błąd wejścia/wyjścia");
                    output.flush();
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            output.println("error: " + e.getMessage());
            output.flush();
        }
    }
}
