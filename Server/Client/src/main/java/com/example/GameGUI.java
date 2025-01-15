package com.example;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;


public class GameGUI extends VBox{

    private HashMap<Position, Circle> circles;
    private Circle circle;
    private HBox hbox;
    private BoardCreator creator;
    public GameGUI(Client client){
        circles = new HashMap<>();

        creator = new BoardCreator();
        creator.setClient(client);
        creator.create(this, circles);
    }
    public void refresh(){
        Platform.runLater(() -> creator.create(this, circles));
    }
}