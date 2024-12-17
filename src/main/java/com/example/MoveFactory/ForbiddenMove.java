package com.example.MoveFactory;

public class ForbiddenMove implements MoveHandler {
    @Override
    public void handle() {
        System.out.println("Error, Forbidden move!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}