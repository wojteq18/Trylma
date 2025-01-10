package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
    public static void main(String[] args) {
        try
        {

        
            Socket socket = new Socket("127.0.0.1", 9999);
            System.out.println("Polaczono z serwerem");

            //tworzy klon strumienia dla watku odbierajacaego wiadomosci od serwera
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //tworzy watek odbierajacy wiadomosci od serwera
            Thread t = new Thread(() -> {
                try {
                    String msg;
                    boolean isReady = false; // Flaga oznaczająca, czy gra może się rozpocząć

                    while ((msg = serverInput.readLine()) != null) {
                        if (!isReady) {
                            // Obsługa wiadomości w stanie "lobby"
                            if (msg.equals("Oczekiwanie na pozostałych graczy...")) {
                                System.out.println("Oczekiwanie na graczy"); // Wiadomość oczekiwania na graczy
                            } else if (msg.equals("Wszyscy gracze dołączyli, gra się rozpoczyna!")) {
                                System.out.println(msg); // Gra się rozpoczyna
                                isReady = true; // Oznacz jako gotowe do gry
                            } else {
                                System.out.println("Błąd"); // Nieznana wiadomość w lobby
                            }
                        } else {
                            // Obsługa wiadomości po rozpoczęciu gry
                            System.out.println(msg);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Błąd odczytu od serwera " + e.getMessage());
                }
            });


            t.start();

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            boolean isRunning = true;
            while (isRunning) {
                String input = scanner.nextLine();
                output.println(input); //wyslanie tesktu de serwera 
                if ("exit".equals(input)) {
                    isRunning = false;
                }
            }
            socket.close();
            scanner.close();
            System.out.println("Rozlaczono z serwerem");
        }
        catch (IOException e) {
            System.out.println("Blad polaczenia z serwerem " + e.getMessage());
        }
    }
}