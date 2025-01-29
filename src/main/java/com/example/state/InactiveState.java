package com.example.state;

import com.example.Player;

public class InactiveState implements PlayerState {
    @Override
    public void handle(Player player) {
        System.out.println("Gracz " + player.getColor() + " jest nieaktywny.");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String getStateName() {
        return "INACTIVE";
    }
}