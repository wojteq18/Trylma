package com.example;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameGUI extends VBox {

    private HashMap<Position, Circle> circles;
    private BoardCreator creator;
    private Client client;

    @Autowired
    public GameGUI(BoardCreator creator) { // Usuwamy Client z konstruktora
        this.creator = creator;
        this.circles = new HashMap<>();
    }

    public void setClient(Client client) {
        this.client = client;
        this.creator.setClient(client);
        this.creator.create(this, circles);
    }

    public void refresh() {
        Platform.runLater(() -> creator.create(this, circles));
    }
}
