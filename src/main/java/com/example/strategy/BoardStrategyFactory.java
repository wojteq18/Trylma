package com.example.strategy;

public class BoardStrategyFactory {
    public static BoardStrategy getStrategy(int strategy) {
        switch (strategy) {
            case 1:
                return new FilledBoardStrategy();
            case 2:
                return new YinBoardStrategy();
            case 3:
                return new ChaosBoardStrategy();
            case 4:
                //TODO logika wczytywania planszy z zapisu    
            default:
                throw new IllegalArgumentException("Invalid strategy: " + strategy);
        }
    }
}
