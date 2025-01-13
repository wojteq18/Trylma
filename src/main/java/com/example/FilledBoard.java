package com.example;

import java.util.ArrayList;
import java.util.List;

public class FilledBoard extends Board{


    public FilledBoard(int length, int numberOfPlayers){
        super(length);
        fillBoard(length, numberOfPlayers);
    }

    public void fillBoard(int length, int numberOfPlayers){
        pawns = new ArrayList<>();
        switch (numberOfPlayers){
            case 2:
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.WHITE, squares, pawns);
                break;
            case 3:
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                break;
            case 4:
                setPawnOn(Colors.BLUE, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                setPawnOn(Colors.GREEN, squares, pawns);
                break;
            case 6:
                setPawnOn(Colors.BLUE, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                setPawnOn(Colors.GREEN, squares, pawns);
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.WHITE, squares, pawns);
                break; 
            default:
                System.err.println("Wrong amount of players!");             
        }
    }
    public void setPawnOn (Colors color, List <Square> squares, List<Pawn> pawns){
        for (Square square : squares){
            if (square.getColor() == color){
                pawns.add(new Pawn(square.getX(), square.getY(), color));
                square.setStatus(false);
            }
        }
    }

    public void printAllCoordinates() {
        StringBuilder sb = new StringBuilder("[");
        for (Pawn pawn : pawns) {
            sb.append("(").append(pawn.getX()).append(", ").append(pawn.getY()).append("), ");
        }
        if (!pawns.isEmpty()) {
            sb.setLength(sb.length() - 2); // Usuń ostatni przecinek i spację
        }
        sb.append("]");
        System.out.println(sb);
    }
}