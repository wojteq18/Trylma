package com.example.state;

import com.example.Player;

public interface PlayerState {
    void handle(Player player);
    boolean isActive();
    String getStateName();
}