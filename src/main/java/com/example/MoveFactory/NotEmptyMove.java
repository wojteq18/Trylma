package com.example.MoveFactory;

public class NotEmptyMove implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public NotEmptyMove(int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, Final square is not empty!");
        System.out.flush();
    }
}