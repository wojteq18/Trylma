package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Lobby extends VBox {
    private Label waitingLabel;
    private Label lobbyLabel;

    public Lobby() {
        // Tworzenie etykiet z tekstem
        waitingLabel = new Label("Oczekiwanie na pozostałych graczy...");
        waitingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        waitingLabel.setTextFill(Color.DARKBLUE);

        lobbyLabel = new Label("Znajdujesz się w lobby.");
        lobbyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lobbyLabel.setTextFill(Color.DARKGREEN);

        // Ustawienia układu
        this.setSpacing(20); // Większy odstęp między elementami
        this.setAlignment(Pos.CENTER); // Wyśrodkowanie elementów w VBox
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #cce7ff); "
                + "-fx-padding: 20; "
                + "-fx-border-color: #336699; "
                + "-fx-border-width: 2; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10;");

        // Dodanie etykiet do VBox
        this.getChildren().addAll(waitingLabel, lobbyLabel);
    }

    public void setWaitingMessage(String message) {
        waitingLabel.setText(message);
    }

    public void setLobbyMessage(String message) {
        lobbyLabel.setText(message);
    }
}
