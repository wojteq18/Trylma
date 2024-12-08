package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int maxPlayers = scanner.nextInt(); // Odbierz liczbę graczy z serwera
        new Game(5, maxPlayers); // Tworzenie planszy z podaną liczbą graczy
    }
}
