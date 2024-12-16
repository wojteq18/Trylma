package com.example.MoveFactory;

public class EscapeMove implements MoveHandler {
    private int queue;
    private int numberOfPlayers;

    public EscapeMove (int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("Error, Pawn is trying to escape from final area!");
        System.out.flush();
    }
}