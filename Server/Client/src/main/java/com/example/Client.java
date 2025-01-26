package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.example.DB.MoveService;
import com.example.DB.SaveService;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Client extends Application {

    private Lobby lobby;
    private GameGUI game;
    private Stage primaryStage;
    private boolean isReady = false;
    private String coordinates = "";
    private String messegToSend = "";
    private List<String> allCoordinates = new ArrayList<>();
    private int numberOfPlayers;
    private String saveName;
    private SaveService saveService;
    private MoveService moveService;

    private static SpringContext springContext; // Referencja do kontekstu Springa

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Pobranie GameGUI z kontekstu Springa
        game = springContext.getBean(GameGUI.class);

        // Utworzenie lobby
        lobby = new Lobby();
        lobby.setClient(this);

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
                        else if (msg.startsWith("Klienci: ")) {
                            numberOfPlayers = Integer.parseInt(msg.substring(9));
                        }
                        else if (msg.startsWith("SAVE_NAME: ")) {
                            String saveName = msg.substring(11);
                            saveService = springContext.getBean(SaveService.class);
                            int max_player = saveService.getNumberOfPlayers(saveName);
                            output.println(max_player);
                            System.out.flush();
                            int numberOfMoves = saveService.getMoveCount(saveName);
                            moveService = springContext.getBean(MoveService.class);
                            String savedBoard = moveService.getMoveData(saveName, numberOfMoves - 1);
                            output.println(savedBoard);
                            System.out.flush();
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

    public void startReplay(String saveName) {
        game.setClient(this);  
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            Scene gameScene = new Scene(game, 800, 600);
            primaryStage.setScene(gameScene);

            MoveService moveService = springContext.getBean(MoveService.class);
            SaveService saveService = springContext.getBean(SaveService.class);
            int len = saveService.getSave(saveName).getMoveCount();
            watchReplay(0, len, saveName, moveService);
        });
        delay.play();

    }

    private void watchReplay(int index, int total, String saveName, MoveService moveService) {
        if (index >= total) {
            return;
        }

        String boardState = moveService.getMoveData(saveName, index);

        System.out.println("Replay [" + index + "]: " + boardState);
        this.coordinates = boardState;
        game.refresh();

        PauseTransition transition = new PauseTransition(Duration.seconds(2));

        transition.setOnFinished(e -> {
            watchReplay(index + 1, total, saveName, moveService);
        });
        transition.play();
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

    public synchronized int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public synchronized String getSaveName() {
        return saveName;
    }

    public static void main(String[] args) {
        springContext = new SpringContext(App.class);
        Application.launch(Client.class, args);
    }
}
