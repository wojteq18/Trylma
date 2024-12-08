package com.example;

import java.util.HashSet;
import java.util.Set;

public class Pawn {

    private int x, y;
    private Colors color;
    private Set<Move> moves = new HashSet<>();

    public Pawn(int x, int y, Colors color){
        this.x = x;
        this.y = y;
        this.color = color;
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
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void addMove(Move move){
        moves.add(move);
    }
    public void removeMove(Move move){
        moves.remove(move);
    }
    public boolean checkMove(Move move){
        if (moves.contains(move)){
            return true;
        } else return false;
    }
    public void clearMoves(){
        moves.clear();
    }
    public Set<Move> getMoves(){
        return moves;
    }
}