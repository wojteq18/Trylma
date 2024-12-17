package com.example.MoveFactory;

public class UnknownError implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("Error, Unknown error!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}