package com.example;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardCreator {
    int[] firstClickCoords = new int[2];
    boolean[] isFirstClick = {true};
    private Client client;

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

            //tablica przechowujaca wspolrzedne pierwszego klikniecia
            circle.setOnMouseClicked(e ->{
                if (isFirstClick[0] == true) {
                    firstClickCoords[0] = i;
                    firstClickCoords[1] = j;
                    isFirstClick[0] = false;
                } else {
                    String move = String.format("move %d %d %d %d", firstClickCoords[0], firstClickCoords[1], i, j);
                    sendMove(move);
                    isFirstClick[0] = true;
                }
            });
            hbox.getChildren().add(circle);
            hbox.setAlignment(Pos.CENTER);
        }

        public String getMessage(String message) {
            return message;
        }

        public void setClient(Client client) {
            this.client = client;
        }

        public void sendMove(String move) {
            if (client != null) {
                client.setMessageToSend(move);
            } else {
                System.out.println("Client is null");
            }
        }
}