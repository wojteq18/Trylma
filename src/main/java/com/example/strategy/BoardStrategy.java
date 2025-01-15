package com.example.strategy;

import com.example.Board;

public interface BoardStrategy {
    Board createBoard(int length, int numberOfPlayers);
}
