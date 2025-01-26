package com.example;

import java.util.ArrayList;

/**
 * The LoadBoard class extends Board and provides functionality to load a board configuration
 * from a string representation.
 */
public class BoardLoader extends Board {

    /**
     * Constructs a LoadBoard with the specified length and initializes it.
     *
     * @param length the length of the board
     */
    public BoardLoader(int length, int numberOfPlayers, String savedString) {
        super(length);
        loadBoardFromString(savedString);
    }

    /**
     * Loads a board configuration from a string.
     * The string should contain tuples in the format (x, y, COLOR),
     * separated by commas.
     *
     * @param boardString the string representation of the board
     */
    public void loadBoardFromString(String boardString) {
    if (pawns == null) {
        pawns = new ArrayList<>();
    }

    boardString = boardString.replace("(", "").replace(")", "").trim();
    String[] elements = boardString.split(",");

    for (int i = 0; i < elements.length; i += 3) {
        try {
            int x = Integer.parseInt(elements[i].trim());
            int y = Integer.parseInt(elements[i + 1].trim());
            Colors color = Colors.valueOf(elements[i + 2].trim().toUpperCase());

            Square square = getSquareAt(x, y);

            if (square != null) {
                square.setColor(color); 
                square.setStatus(false);

                Pawn pawn = new Pawn(x, y, color);
                pawns.add(pawn);
            }
        } catch (Exception e) {
            System.err.println("Error processing elements: "
                    + elements[i] + ", "
                    + elements[i + 1] + ", "
                    + elements[i + 2]);
        }
    }
}

    /**
     * Finds a square on the board by its coordinates.
     *
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @return the square at the specified coordinates, or null if not found
     */
    private Square getSquareAt(int x, int y) {
        for (Square square : squares) {
            if (square.getX() == x && square.getY() == y) {
                return square;
            }
        }
        return null;
    }
}
