package com.example;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import com.example.DB.SaveService;
import com.example.DB.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardCreator {
    int[] firstClickCoords = new int[2];
    boolean[] isFirstClick = {true};
    private Client client;
    private TextField saveField;
    @Autowired
    private SaveService saveService;
    @Autowired
    private MoveService moveService;


        public void create(VBox root, HashMap<Position, Circle> circles) {
            Platform.runLater(() -> {
            root.getChildren().clear();
            HBox upper = new HBox(10);
            HBox lower = new HBox(10);
            Button button = new Button("Wait");
            Button button2 = new Button("Save");            
            upper.getChildren().add(button);
            lower.getChildren().add(button2);
            upper.setAlignment(Pos.CENTER);
            lower.setAlignment(Pos.BOTTOM_RIGHT);
            root.getChildren().add(upper);
            root.getChildren().add(lower);
            button.setOnAction(e ->{
                    String wait = "wait";
                    sendMove(wait);
            });
            
            button2.setOnAction(e -> {
                TextField inputField = new TextField();
                inputField.setPromptText("Enter name");
                inputField.setMaxWidth(200);
                lower.getChildren().add(inputField);

                Button sendButton = new Button("Send");
                lower.getChildren().add(sendButton);

                sendButton.setOnAction(event -> {
                    String enteredName = inputField.getText();
                    int moveCount = client.getAllCoordinates().size(); 

                    if (saveService != null && !enteredName.isEmpty()) {
                        // Zapisanie do bazy danych
                        saveService.addSave(enteredName, moveCount);
                        for (int i = 0; i < moveCount; i++) {
                            String moveData = client.getAllCoordinates().get(i);
                            moveService.addMove(enteredName, i, moveData);
                        }
                    } else {
                        System.out.println("SaveService is null or name is empty!");
                    }

                    // Usunięcie pola tekstowego i przycisku po zapisaniu
                    lower.getChildren().remove(inputField);
                    lower.getChildren().remove(sendButton);
                });
            });



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
            if (client != null) draw(circles, client);
        });
        }

        public void createRow(HBox hbox, Color fillColor, Color strokeColor, int i, int j, HashMap<Position, Circle> circles) {
            Circle circle = new Circle(15);
            circle.setStroke(strokeColor);
            circle.setFill(fillColor);
            circles.put(new Position(i, j), circle);

            //tablica przechowujaca wspolrzedne pierwszego klikniecia
            circle.setOnMouseClicked(e ->{
                if (isFirstClick[0] == true) {
                    circle.setStroke(Color.PURPLE);
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
        public void draw(HashMap<Position, Circle> circles, Client client){
            String message = client.getCoordinates();

            Pattern pattern = Pattern.compile("\\((-?\\d+),\\s*(-?\\d+),\\s*(\\w+)\\)");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                String color = matcher.group(3);
                Circle circle = circles.get(new Position(x, y));
                if(circle != null){
                    switch (color) {
                        case "WHITE":
                            circle.setFill(Color.WHITE);
                            break;
                        case "BLACK":
                            circle.setFill(Color.BLACK);
                            break;
                        case "YELLOW":
                            circle.setFill(Color.YELLOW);
                            break;
                        case "GREEN":
                            circle.setFill(Color.GREEN);
                            break;
                        case "BLUE":
                            circle.setFill(Color.BLUE);
                            break;
                        case "RED":
                            circle.setFill(Color.RED);
                            break;
                        default:
                            break;
                    }   
                }
            }
        }

}