package com.example.MoveFactory;

public class NoPawnMove implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("Error, There is no pawn there!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}   