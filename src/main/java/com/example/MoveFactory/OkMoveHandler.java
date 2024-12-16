package com.example.MoveFactory;

public class OkMoveHandler implements MoveHandler {
    private int queue;
    private int numberOfPlayers;
    
    public OkMoveHandler(int numberOfPlayers, int queue) {
        this.numberOfPlayers = numberOfPlayers;
        this.queue = queue;
    }

    @Override
    public void handle() {
        System.out.println("ok");
        System.out.flush();
        queue = (queue + 1) % numberOfPlayers;
    }
}

