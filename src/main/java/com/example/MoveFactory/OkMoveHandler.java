package com.example.MoveFactory;

public class OkMoveHandler implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("ok");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return true;
    }
}

