package com.example;

public class PlayerManager{

    private Player[] players;

    public PlayerManager(int numberOfPlayers, Board board){
        createPlayers(numberOfPlayers, board);
    }
    public Player[] getPlayers(){
        return players;
    }
    public void createPlayers(int numberOfPlayers, Board board){
        
        switch (numberOfPlayers){
            case 2:
                players = new Player[2];
                players[1] = new Player(Colors.WHITE, board, State.ACTIVE);
                players[0] = new Player(Colors.BLACK, board, State.ACTIVE);
                break;
            case 3:
                players = new Player[3];
                players[1] = new Player(Colors.BLACK, board, State.ACTIVE);
                players[2] = new Player(Colors.YELLOW, board, State.ACTIVE);
                players[0] = new Player(Colors.RED, board, State.ACTIVE);
                break;
            case 4:
                players = new Player[4];
                players[0] = new Player(Colors.RED, board, State.ACTIVE);
                players[3] = new Player(Colors.YELLOW, board, State.ACTIVE);
                players[1] = new Player(Colors.GREEN, board, State.ACTIVE);
                players[2] = new Player(Colors.BLUE, board, State.ACTIVE);
                break;
            case 6:
                players = new Player[6];
                players[1] = new Player(Colors.RED, board, State.ACTIVE);
                players[5] = new Player(Colors.YELLOW, board, State.ACTIVE);
                players[2] = new Player(Colors.GREEN, board, State.ACTIVE);
                players[4] = new Player(Colors.BLUE, board, State.ACTIVE);
                players[3] = new Player(Colors.BLACK, board, State.ACTIVE);
                players[0] = new Player(Colors.WHITE, board, State.ACTIVE);
                break;
            //default:
                //throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);    
        }
    }
    public int activePlayers(Player[] players){
        int size = players.length;
        int count = 0;
        for (int i = 0; i < size; i++){
            if (players[i].getState() == State.ACTIVE){
                count++;
            }
        }
        return count;
    }
}