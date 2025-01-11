package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int maxPlayers = scanner.nextInt(); // Odbierz liczbę graczy z serwera
        int strategy = scanner.nextInt(); //mam nadzieje ze nie zepsulem koncepcji
        new Game(5, maxPlayers, strategy); // Tworzenie planszy z podaną liczbą graczy, TODO - dodac strategie
        scanner.close();
    }
}
