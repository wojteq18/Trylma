package com.example.strategy;

import com.example.Board;
import com.example.ChaosBoard;

public class ChaosBoardStrategy implements BoardStrategy {
    @Override
    public Board createBoard(int length, int numberOfPlayers) {
        return new ChaosBoard(length, numberOfPlayers);
    }
}
