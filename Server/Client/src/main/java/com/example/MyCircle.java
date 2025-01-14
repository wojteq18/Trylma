package com.example;


import javafx.scene.shape.Circle;

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