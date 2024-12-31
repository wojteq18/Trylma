package com.example;

import java.util.ArrayList;
import java.util.List;

public class Board {

    protected List <Square> squares;
    private int length;

    public Board(int length){
        this.length = length;
        createBoard(length);
    }

    public int getLength() {
        return length;
    }

    public void createBoard(int length){
        squares = new ArrayList<>();
        for (int i = 0; i <= length - 2; i++){
            for (int j = -i; j <= i; j = j + 2){
                squares.add(new Square(i, j, Colors.WHITE, true));
            }
        }
        for (int i = length - 1; i <= 2*length - 2; i++){
            for (int j = -i; j <= i; j = j + 2){
                squares.add(new Square(i, j, Colors.NULL, true));  
            }
        }
        int count = 2;
        for (int i = 2*length - 1; i <= 3*length - 3; i++){
            for (int j = -i+count; j <= i-count; j = j + 2){
                squares.add(new Square(i, j, Colors.NULL, true));
            }
            count+=2;
        }
        count = 0;
        for (int i = 3*length - 2; i <= (length - 1)*4; i++){
            for (int j = -i + 2*length + count ; j <= i - 2*length - count; j = j + 2){
                squares.add(new Square(i, j, Colors.BLACK, true));
            }
            count+=2;
        }
        count = 0;
        for (int i = length - 1; i <= 2*length - 3; i++){
            for (int j = 2 * i + count; j <= 2 * i + 6 + 3 * count; j = j + 2){
                squares.add(new Square(i, j, Colors.YELLOW, true));
            }
            count -=1;
        }
        count = 0;
        for (int i = length - 1; i <= 2*length - 3; i++){
            for (int j = -(2 * i + 6 + 3 * count); j <= -(2 * i + count); j = j + 2){
                squares.add(new Square(i, j, Colors.RED, true));
            }
            count -=1;
        }
        count = 0;
        for (int i = 2*length - 1; i <= 3*length - 3; i++){
            for (int j = -i; j <= -i + count; j = j + 2){
                squares.add(new Square(i, j, Colors.BLUE, true));
            }
            count += 2;
        }
        count = 0;
        for (int i = 2*length - 1; i <= 3*length - 3; i++){
            for (int j = i - count; j <= i; j = j + 2){
                squares.add(new Square(i, j, Colors.GREEN, true));
            }
            count += 2;
        }

    }
    public List<Square> getSquares(){
        return squares;
    }
}