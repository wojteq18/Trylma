package com.example;

import java.util.HashMap;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class GameGUI extends VBox{

    private HashMap<Position, Circle> circles;
    private Circle circle;
    private HBox hbox;
    public GameGUI(Client client){
        circles = new HashMap<>();

        BoardCreator creator = new BoardCreator();
        creator.create(this, circles);
        creator.setClient(client);

        PawnFiller filler = new PawnFiller();
        filler.fill(this, circles);
    }
}