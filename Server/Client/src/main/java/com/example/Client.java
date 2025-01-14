package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    private Lobby lobby;
    private boolean isReady = false;

    @Override
    public void start(Stage primaryStage) {
        // Utworzenie lobby
        lobby = new Lobby();

        // Konfiguracja sceny i okna
        Scene scene = new Scene(lobby, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lobby");
        primaryStage.show();

        // Rozpoczęcie klienta sieciowego w osobnym wątku
        new Thread(this::startClient).start();
    }

    private void startClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            System.out.println("Połączono z serwerem");

            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            //Wątek do odbierania wiadomosci od serwera
            Thread receiveThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = serverInput.readLine()) != null) {
                        if (msg.equals("Wszyscy gracze dołączyli, gra się rozpoczyna!")) {
                            isReady = true;
                        }
                        else if (msg.equals("Oczekiwanie na pozostałych graczy...")) {
                            lobby.setWaitingMessage(msg);
                        }
                        if (isReady == true) {
                            System.out.println(msg);
                        }
                    }
                    
                } catch (IOException e) {
                    System.out.println("Błąd podczas odbierania wiadomosci: " + e.getMessage());
                }
            });
            receiveThread.start();

            //Wątek do wysyłania wiadomości do serwera
            Thread sendThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                try {
                    boolean isRunning = true;
                    while (isRunning) { 
                        String message = scanner.nextLine();
                        output.println(message);
                        if ("exit".equalsIgnoreCase(message)) {
                            isRunning = false;
                        } 
                    }
                    scanner.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Błąd podczas wysyłania wiadomości: " + e.getMessage());
                }
            });
            sendThread.start();

            //Czekaj na zakończenie wątków
            receiveThread.join();
            sendThread.join();


        } catch (IOException | InterruptedException e) {
            System.out.println("Błąd klienta: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}