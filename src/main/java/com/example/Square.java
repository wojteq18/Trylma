package com.example;

public class Square{
    private int y;
    private int x;
    private Colors color;
    private boolean isEmpty;

    public Square(int x, int y, Colors color, boolean isEmpty){
        this.x = x;
        this.y = y;
        this.color = color;
        this.isEmpty = isEmpty;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Colors getColor(){
        return color;
    }
    public boolean getStatus(){
        return isEmpty;
    }
    public void setStatus(boolean isEmpty){
        this.isEmpty = isEmpty;
    }
}