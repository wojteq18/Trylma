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
        public void create(VBox root, List<Circle> circles) {
            for (int i = 1; i <= 4; i++) {
                HBox hbox = new HBox(10);
                createRow(hbox, i, Color.GREY, Color.WHITE, circles);
                root.getChildren().add(hbox);
            }

            for (int i = 13; i >= 9; i--) {
                HBox hbox = new HBox(10);
                createRow(hbox, i, Color.GREY, Color.GREY, circles);
                root.getChildren().add(hbox);
            }

            for (int i = 10; i <= 13; i++) {
                HBox hbox = new HBox(10);
                createRow(hbox, i, Color.GREY, Color.GREY, circles);
                root.getChildren().add(hbox);
            }

            for (int i = 4; i >= 1; i--) {
                HBox hbox = new HBox(10);
                createRow(hbox, i, Color.GREY, Color.BLACK, circles);
                root.getChildren().add(hbox);
            }

            root.setAlignment(Pos.CENTER);
        }

        private void createRow(HBox hbox, int numCircles, Color fillColor, Color strokeColor, List<Circle> circles) {
            for (int j = 1; j <= numCircles; j++) {
                Circle circle = new Circle(15);
                circles.add(circle);
                circle.setStroke(strokeColor);
                circle.setFill(fillColor);
                circle.setOnMouseClicked(e ->{
                    System.out.println("klik");
                });
                hbox.getChildren().add(circle);
            }
            hbox.setAlignment(Pos.CENTER);
        }
}
