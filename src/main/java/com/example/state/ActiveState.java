package com.example.state;

import com.example.Player;

public class ActiveState implements PlayerState {
    @Override
    public void handle(Player player) {
        System.out.println("Gracz " + player.getColor() + " jest aktywny.");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getStateName() {
        return "ACTIVE";
    }
}