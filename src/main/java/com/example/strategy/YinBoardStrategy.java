package com.example.strategy;

import com.example.Board;
import com.example.YinBoard;

public class YinBoardStrategy implements BoardStrategy {
    @Override
    public Board createBoard(int length, int numberOfPlayers) {
        return new YinBoard(length, numberOfPlayers);
    }
}
