package com.example.MoveFactory;

public class NotEmptyMove implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("Error, Final square is not empty!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}