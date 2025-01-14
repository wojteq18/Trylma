package com.example;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class BoardCreator {
        public void create(VBox root, HashMap<Position, Circle> circles) {

                for (int i = 0; i <= 3; i++) {
                    HBox hbox = new HBox(10);
                    for (int j = -i; j <= i; j = j + 2)
                    createRow(hbox, Color.GREY, Color.WHITE, i, j, circles);
                    root.getChildren().add(hbox);
                }
                int count = 0;
                for (int i = 4; i <= 8; i++) {
                    HBox hbox = new HBox(10);
                    for (int j = -12 + count; j <= 12 - count; j = j + 2)
                    createRow(hbox, Color.GREY, Color.WHITE, i, j, circles);
                    root.getChildren().add(hbox);
                    count++;
                }
                count = 0;
                for (int i = 9; i <= 12; i++) {
                    HBox hbox = new HBox(10);
                    for (int j = -9 - count; j <= 9 +count; j = j + 2)
                    createRow(hbox, Color.GREY, Color.WHITE, i, j, circles);
                    root.getChildren().add(hbox);
                    count++;
                }
                count = 0;
                for (int i = 13; i <= 16; i++) {
                    HBox hbox = new HBox(10);
                    for (int j = -3 + count; j <= 3 - count; j = j + 2)
                    createRow(hbox, Color.GREY, Color.WHITE, i, j, circles);
                    root.getChildren().add(hbox);
                    count ++;
                }
            root.setAlignment(Pos.CENTER);
        }

        public void createRow(HBox hbox, Color fillColor, Color strokeColor, int i, int j, HashMap<Position, Circle> circles) {
            Circle circle = new Circle(15);
            circle.setStroke(strokeColor);
            circle.setFill(fillColor);
            circles.put(new Position(i, j), circle);
            circle.setOnMouseClicked(e ->{
                System.out.println("klik" + i + " " + j);
            });
            hbox.getChildren().add(circle);
            hbox.setAlignment(Pos.CENTER);
        }
}
