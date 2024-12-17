package com.example.MoveFactory;

public class NotExistMove implements MoveHandler {

    @Override
    public void handle() {
        System.out.println("Error, Final square does not exists!");
        System.out.flush();
    }

    @Override
    public boolean shouldSwitchPlayer() {
        return false;
    }
}