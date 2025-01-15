package com.example;

import java.util.ArrayList;

/**
 * The YinBoard class represents a special type of board configuration 
 * designed specifically for a two-player game.
 */
public class YinBoard extends Board {

    /**
     * Constructs a YinBoard with the specified length and number of players.
     * Throws an exception if the number of players is not exactly 2.
     *
     * @param length          the length of the board
     * @param numberOfPlayers the number of players, must be 2
     * @throws IllegalArgumentException if the number of players is not 2
     */
    public YinBoard(int length, int numberOfPlayers) {
        super(length);
        if (numberOfPlayers != 2) {
            throw new IllegalArgumentException("Yin error!");
        }
        createYin();
    }

    /**
     * Creates the initial YinBoard configuration by placing pawns.
     * The pawns are assigned to specific colors.
     */
    public void createYin() {
        pawns = new ArrayList<>();
        setPawn(Colors.BLACK);
        setPawn(Colors.GREEN);
    }

    /**
     * Places pawns of the specified color on the board based on the square colors.
     * 
     * @param color the color of the pawns to place
     */
    public void setPawn(Colors color) {
        for (Square square : squares) {
            if (square.getColor() == color) {
                pawns.add(new Pawn(square.getX(), square.getY(), color));
                square.setStatus(false);
            }
        }
    }

    /**
     * Prints the coordinates and colors of all pawns on the board to the console.
     * The output includes each pawn's position and color in a readable format.
     */
    public void printAllCoordinates() {
        StringBuilder sb = new StringBuilder();
        for (Pawn pawn : pawns) {
            sb.append("(").append(pawn.getX()).append(", ").append(pawn.getY()).append(", ").append(pawn.getColor()).append("), ");
        }
        if (!pawns.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        System.out.println(sb);
    }
}
