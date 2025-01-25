package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Lobby extends VBox {
    private Label waitingLabel;
    private Label lobbyLabel;
    private TextField nicknameField;
    private TextField replayField;
    private Button sendButton;
    private Button replayButton;
    private Button send;
    private String nickname;
    private Client client;

    public Lobby() {
        // Tworzenie etykiet z tekstem
        waitingLabel = new Label("Oczekiwanie na pozostałych graczy...");
        waitingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        waitingLabel.setTextFill(Color.DARKBLUE);

        lobbyLabel = new Label("Znajdujesz się w lobby.");
        lobbyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lobbyLabel.setTextFill(Color.DARKGREEN);

        // Pole tekstowe do wpisywania nicku
        nicknameField = new TextField();
        nicknameField.setPromptText("Wpisz swój nick");
        nicknameField.setMaxWidth(200);

        // Guzik do zatwierdzania nicku
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            nickname = nicknameField.getText();
            if (!nickname.isEmpty()) {
                lobbyLabel.setText("Twój nick: " + nickname);
            } else {
                lobbyLabel.setText("Nick nie może być pusty. Wpisz swój nick.");
            }
        });

        //guzik do obejrzenia powtórki
        replayButton = new Button("Replay");
        replayButton.setOnAction(e -> {
            replayField = new TextField();
            replayField.setPromptText("Enter save name");
            replayField.setMaxWidth(200);
            this.getChildren().add(replayField);
            sendButton = new Button("Send");
            this.getChildren().add(sendButton);
            sendButton.setOnAction(event -> {
                String saveName = replayField.getText();
                if (!saveName.isEmpty()) {
                    System.out.println("Replay: " + saveName);
                    client.startReplay(saveName);
                } else {
                    lobbyLabel.setText("Save name cannot be empty. Enter save name.");
                }
            });
        });

        // Ustawienia układu
        this.setSpacing(20); // Większy odstęp między elementami
        this.setAlignment(Pos.CENTER); // Wyśrodkowanie elementów w VBox
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #cce7ff); "
                + "-fx-padding: 20; "
                + "-fx-border-color: #336699; "
                + "-fx-border-width: 2; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10;");

        // Dodanie elementów do VBox
        this.getChildren().addAll(waitingLabel, lobbyLabel, nicknameField, sendButton, replayButton);
    }

    public void setWaitingMessage(String message) {
        waitingLabel.setText(message);
    }

    public void setLobbyMessage(String message) {
        lobbyLabel.setText(message);
    }

    public String getNickname() {
        return nickname;
    }

    public String getSaveName() {
        return replayField.getText();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}