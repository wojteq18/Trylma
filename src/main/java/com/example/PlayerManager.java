package com.example;

import com.example.state.ActiveState;

public class PlayerManager{

    private Player[] players;

    public PlayerManager(int numberOfPlayers, Board board, boolean bot){
        createPlayers(numberOfPlayers, board, bot);
    }
    public Player[] getPlayers(){
        return players;
    }
    public void createPlayers(int numberOfPlayers, Board board, boolean bot){
        
        switch (numberOfPlayers){
            case 2:
                players = new Player[2];
                players[0] = new Player(Colors.WHITE, board, new ActiveState(), false);
                players[1] = new Player(Colors.BLACK, board, new ActiveState(), false);
                break;
            case 3:
                players = new Player[3];
                players[1] = new Player(Colors.BLACK, board, new ActiveState(), false);
                players[2] = new Player(Colors.YELLOW, board, new ActiveState(), false);
                players[0] = new Player(Colors.RED, board, new ActiveState(), false);
                break;
            case 4:
                players = new Player[4];
                players[0] = new Player(Colors.RED, board, new ActiveState(), false);
                players[3] = new Player(Colors.YELLOW, board, new ActiveState(), false);
                players[1] = new Player(Colors.GREEN, board, new ActiveState(), false);
                players[2] = new Player(Colors.BLUE, board, new ActiveState(), false);
                break;
            case 6:
                players = new Player[6];
                players[1] = new Player(Colors.RED, board, new ActiveState(), false);
                players[5] = new Player(Colors.YELLOW, board, new ActiveState(), false);
                players[2] = new Player(Colors.GREEN, board, new ActiveState(), false);
                players[4] = new Player(Colors.BLUE, board, new ActiveState(), false);
                players[3] = new Player(Colors.BLACK, board, new ActiveState(), false);
                players[0] = new Player(Colors.WHITE, board, new ActiveState(), false);
                break;
            default:
                throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);    
        }
        if (bot){
            for (int i = 0; i < players.length; i++){
                players[i].setBot(true);
            }
            players[0].setBot(false);
        }
    }
    public int activePlayers(Player[] players) {
        int count = 0;
        for (Player player : players) {
            if (player.isActive()) {
                count++;
            }
        }
        return count;
    }
}