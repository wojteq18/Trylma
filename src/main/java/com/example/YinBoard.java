package com.example;

import java.util.ArrayList;

public class YinBoard extends Board{

    public YinBoard(int length, int numberOfPlayers){
        super(length);
        if (numberOfPlayers != 2){
            throw new IllegalArgumentException("Yin error!");
        }
        createYin();
    }
    public void createYin(){
        pawns = new ArrayList<>();
        setPawn(Colors.BLACK);
        setPawn(Colors.GREEN);
    }
    public void setPawn (Colors color){
        for (Square square : squares){
            if (square.getColor() == color){
                pawns.add(new Pawn(square.getX(), square.getY(), color));
                square.setStatus(false);
            }
        }
    }

    public void printAllCoordinates() {
        StringBuilder sb = new StringBuilder();
        for (Pawn pawn : pawns) {
            sb.append("(").append(pawn.getX()).append(", ").append(pawn.getY()).append(", ").append(pawn.getColor()).append("), ");
        }
        if (!pawns.isEmpty()) {
            sb.setLength(sb.length() - 2); // Usuń ostatni przecinek i spację
        }
        System.out.println(sb);
    }
}