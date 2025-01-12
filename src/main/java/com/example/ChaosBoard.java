package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaosBoard extends Board {

    public ChaosBoard (int length, int numberOfPlayers){
        super(length);
        createChaos(numberOfPlayers);
    }

    public void createChaos(int numberOfPlayers){

        switch (numberOfPlayers) {
            case 2:
                setPawn(Colors.BLACK);
                setPawn(Colors.WHITE);
                break;
            case 3:
                setPawn(Colors.BLACK);
                setPawn(Colors.YELLOW);
                setPawn(Colors.RED);
                break;
            case 4:
                setPawn(Colors.BLUE);
                setPawn(Colors.YELLOW);
                setPawn(Colors.RED);
                setPawn(Colors.GREEN);
                break;
            case 6:
                setPawn(Colors.BLACK);
                setPawn(Colors.YELLOW);
                setPawn(Colors.WHITE);
                setPawn(Colors.GREEN);
                setPawn(Colors.RED);
                setPawn(Colors.BLUE);
                break;
            default:
                System.err.println("Wrong amount of players!");             
                break;
        }
        changeSquareColor(Colors.BLACK, Colors.WHITE);
        changeSquareColor(Colors.WHITE, Colors.BLACK);
        changeSquareColor(Colors.BLUE, Colors.YELLOW);
        changeSquareColor(Colors.YELLOW, Colors.BLUE);
        changeSquareColor(Colors.RED, Colors.GREEN);
        changeSquareColor(Colors.GREEN, Colors.RED);


    }
    public void setPawn(Colors color){
        List <Square> chaosSquares = new ArrayList<>();
        for (Square square : squares){
            if (square.getColor() == Colors.NULL && square.getStatus()){
                chaosSquares.add(square);
            }
        }
        Random rand = new Random();
        for (int i = 1; i <= 10; i++){
            int randomIndex = rand.nextInt(chaosSquares.size());
            Square square = chaosSquares.get(randomIndex);
            pawns.add(new Pawn(square.getX(), square.getY(), color));
            square.setStatus(false);
        }
    }
    public void changeSquareColor(Colors start, Colors end){
        for (Square square : squares){
            if(square.getColor() == start){
                square.setColor(end);
            }
        }
    }
}