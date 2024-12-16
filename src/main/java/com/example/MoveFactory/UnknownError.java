package com.example.MoveFactory;

public class UnknownError implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public UnknownError(int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, Unknown error!");
        System.out.flush();
    }
}