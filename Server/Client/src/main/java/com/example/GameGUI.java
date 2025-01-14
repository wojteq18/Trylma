package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class GameGUI extends VBox{

    private HashMap<Position, Circle> circles;
    private Circle circle;
    private HBox hbox;
    public GameGUI(){
        circles = new HashMap<>();
        System.out.println("elo");

        BoardCreator creator = new BoardCreator();
        creator.create(this, circles);

        PawnFiller filler = new PawnFiller();
        filler.fill(this, circles);
    }
}