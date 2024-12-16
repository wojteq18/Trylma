package com.example.MoveFactory;

public class NotExistMove implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public NotExistMove(int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, Final square does not exists!");
        System.out.flush();
    }
}