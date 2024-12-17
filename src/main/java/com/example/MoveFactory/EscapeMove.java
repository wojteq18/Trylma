package com.example.MoveFactory;

public class EscapeMove implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("Error, Pawn is trying to escape from final area!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}