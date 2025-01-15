package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The FilledBoard class represents a game board pre-filled with pawns based on the number of players.
 * Pawns are placed on squares of specific colors corresponding to each player.
 */
public class FilledBoard extends Board {

    /**
     * Constructs a FilledBoard with the specified length and number of players.
     * Initializes the board and fills it with pawns.
     *
     * @param length         the length of the board
     * @param numberOfPlayers the number of players in the game
     */
    public FilledBoard(int length, int numberOfPlayers) {
        super(length);
        fillBoard(length, numberOfPlayers);
    }

    /**
     * Fills the board with pawns based on the number of players and their associated colors.
     *
     * @param length          the length of the board
     * @param numberOfPlayers the number of players in the game
     */
    public void fillBoard(int length, int numberOfPlayers) {
        pawns = new ArrayList<>();
        switch (numberOfPlayers) {
            case 2:
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.WHITE, squares, pawns);
                break;
            case 3:
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                break;
            case 4:
                setPawnOn(Colors.BLUE, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                setPawnOn(Colors.GREEN, squares, pawns);
                break;
            case 6:
                setPawnOn(Colors.BLUE, squares, pawns);
                setPawnOn(Colors.YELLOW, squares, pawns);
                setPawnOn(Colors.RED, squares, pawns);
                setPawnOn(Colors.GREEN, squares, pawns);
                setPawnOn(Colors.BLACK, squares, pawns);
                setPawnOn(Colors.WHITE, squares, pawns);
                break;
            default:
                System.err.println("Wrong amount of players!");
        }
    }

    /**
     * Places pawns on squares of the specified color and updates their status.
     *
     * @param color   the color of the squares where pawns should be placed
     * @param squares the list of squares on the board
     * @param pawns   the list of pawns to be updated
     */
    public void setPawnOn(Colors color, List<Square> squares, List<Pawn> pawns) {
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
