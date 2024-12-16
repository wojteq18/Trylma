package com.example.MoveFactory;

public class ForbiddenMove implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public ForbiddenMove (int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, Forbidden move!");
        System.out.flush();
    }
}