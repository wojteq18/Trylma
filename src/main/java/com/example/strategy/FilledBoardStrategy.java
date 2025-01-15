package com.example.strategy;

import com.example.Board;
import com.example.FilledBoard;

public class FilledBoardStrategy implements BoardStrategy {
    @Override
    public Board createBoard(int length, int numberOfPlayers) {
        return new FilledBoard(length, numberOfPlayers);
    }
}
