package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameGUI extends Pane{

    public GameGUI(){
        VBox box = new VBox(10);
        for (int i = 1; i <=4; i++){
            HBox hbox = new HBox (10);
            for(int j = 1; j <= i; j++){
                Circle circle = new Circle(5);
                circle.setStroke(Color.BLACK);
                hbox.getChildren().add(circle);
            }
            box.getChildren().add(hbox);
        }
        this.getChildren().add(box);
    }
}