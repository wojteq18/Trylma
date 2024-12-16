package com.example.MoveFactory;

public class NoPawnMove implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public NoPawnMove(int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, There is no pawn there!");
        System.out.flush();
    }
}   