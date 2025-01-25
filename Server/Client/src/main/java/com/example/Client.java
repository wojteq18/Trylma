package com.example;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Application {

    private Lobby lobby;
    private GameGUI game;
    private Stage primaryStage;
    private boolean isReady = false;
    private String coordinates = "";
    private String messegToSend = "";
    private List<String> allCoordinates = new ArrayList<>();

    private static SpringContext springContext; // Referencja do kontekstu Springa

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Pobranie GameGUI z kontekstu Springa
        game = springContext.getBean(GameGUI.class);

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
                            startGame();
                        }
                        else if (msg.equals("Oczekiwanie na pozostałych graczy...")) {
                            lobby.setWaitingMessage(msg);
                        }
                        else if (msg.startsWith("(")) {
                            coordinates = msg.trim();
                            allCoordinates.add(coordinates);
                            Platform.runLater(() -> game.refresh());                            
                            System.out.println("Coordinates: " + coordinates);
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
                Platform.runLater(() -> game.refresh());                            
                try {
                    boolean isRunning = true; //zmienione
                    while (isRunning) { 
                        synchronized(this) {
                            if (!messegToSend.isEmpty()) {
                                output.println(messegToSend);
                                output.flush();
                                messegToSend = "";
                            }
                        }
                    }
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

    private void startGame() {
        game.setClient(this);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            Scene gameScene = new Scene(game, 800, 600);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle(lobby.getNickname());
        });
        delay.play();
    }

    public synchronized void setMessageToSend(String message) {
        this.messegToSend = message;
    }

    public synchronized String getCoordinates() {
        return coordinates;
    }

    public synchronized List<String> getAllCoordinates() {
        return allCoordinates;
    }

    public static void main(String[] args) {
        springContext = new SpringContext(App.class);
        Application.launch(Client.class, args);
    }
}
