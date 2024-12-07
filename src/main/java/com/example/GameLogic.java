package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GameLogic {

    private static int currentPlayer = 0;

    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(System.out);

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
                    if (number % 2 != 0) {
                        output.println("error: Liczba musi być parzysta");
                        output.flush();
                        continue;
                    }

                    // Wszystko w porządku, poprawna liczba parzysta
                    output.println("ok: Liczba zaakceptowana od gracza " + (currentPlayer + 1));
                    output.flush();

                    // Przejście do następnego gracza
                    currentPlayer = (currentPlayer + 1) % 2; // Zakładamy, że są tylko 2 graczy
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
    }
}
