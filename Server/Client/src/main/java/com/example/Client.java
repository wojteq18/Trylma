package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Client extends Application {
    private Lobby lobby;
    private GameGUI game;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Utworzenie lobby
        lobby = new Lobby();

        // Konfiguracja sceny i okna
        Scene lobbyscene = new Scene(lobby, 400, 200);
        primaryStage.setScene(lobbyscene);
        primaryStage.setTitle("Trylma");
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

            String msg;
            while ((msg = serverInput.readLine()) != null) {
                String finalMsg = msg;
                javafx.application.Platform.runLater(() -> {
                    if (finalMsg.equals("Oczekiwanie na pozostałych graczy...")) {
                        lobby.setWaitingMessage(finalMsg);
                    } else if (finalMsg.equals("Wszyscy gracze dołączyli, gra się rozpoczyna!")) {
                        lobby.setLobbyMessage(finalMsg);
                        startGame();
                    } else {
                        lobby.setLobbyMessage("Nieznana wiadomość: " + finalMsg);
                    }
                });
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Błąd klienta: " + e.getMessage());
        }
    }
    private void startGame() {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            // Po zakończeniu opóźnienia przechodzimy do gry
            game = new GameGUI();
            Scene gamescene = new Scene(game, 800, 400);
            primaryStage.setScene(gamescene);
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}