package com.example;


import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class MyCircle extends Circle {

    private int x, y;

    public MyCircle(int x, int y, double r){
        super(r);
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}