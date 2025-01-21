package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The Board class represents a game board consisting of squares and pawns.
 * It supports creating a board with specified dimensions and managing its state.
 */
public class Board {

    /**
     * List of squares that make up the board.
     */
    protected List<Square> squares;

    /**
     * List of pawns present on the board.
     */
    protected List<Pawn> pawns;

    /**
     * The length of the board.
     */
    private int length;

    /**
     * Constructs a Board object with the specified length and initializes it.
     *
     * @param length the length of the board
     */
    public Board(int length) {
        this.length = length;
        createBoard(length);
    }

    /**
     * Gets the length of the board.
     *
     * @return the length of the board
     */
    public int getLength() {
        return length;
    }

    /**
     * Creates the board by initializing squares based on the given length.
     *
     * @param length the length of the board
     */
    public void createBoard(int length) {
        squares = new ArrayList<>();
        for (int i = 0; i <= length - 2; i++) {
            for (int j = -i; j <= i; j = j + 2) {
                squares.add(new Square(i, j, Colors.WHITE, true));
            }
        }
        for (int i = length - 1; i <= 2 * length - 2; i++) {
            for (int j = -i; j <= i; j = j + 2) {
                squares.add(new Square(i, j, Colors.NULL, true));
            }
        }
        int count = 2;
        for (int i = 2 * length - 1; i <= 3 * length - 3; i++) {
            for (int j = -i + count; j <= i - count; j = j + 2) {
                squares.add(new Square(i, j, Colors.NULL, true));
            }
            count += 2;
        }
        count = 0;
        for (int i = 3 * length - 2; i <= (length - 1) * 4; i++) {
            for (int j = -i + 2 * length + count; j <= i - 2 * length - count; j = j + 2) {
                squares.add(new Square(i, j, Colors.BLACK, true));
            }
            count += 2;
        }
        count = 0;
        for (int i = length - 1; i <= 2 * length - 3; i++) {
            for (int j = 2 * i + count - 2; j <= 2 * i + 6 + 3 * count - 2; j = j + 2) {
                squares.add(new Square(i, j, Colors.YELLOW, true));
            }
            count -= 1;
        }
        count = 0;
        for (int i = length - 1; i <= 2 * length - 3; i++) {
            for (int j = -(2 * i + 6 + 3 * count) + 2; j <= -(2 * i + count) + 2; j = j + 2) {
                squares.add(new Square(i, j, Colors.RED, true));
            }
            count -= 1;
        }
        count = 0;
        for (int i = 2 * length - 1; i <= 3 * length - 3; i++) {
            for (int j = -i; j <= -i + count; j = j + 2) {
                squares.add(new Square(i, j, Colors.BLUE, true));
            }
            count += 2;
        }
        count = 0;
        for (int i = 2 * length - 1; i <= 3 * length - 3; i++) {
            for (int j = i - count; j <= i; j = j + 2) {
                squares.add(new Square(i, j, Colors.GREEN, true));
            }
            count += 2;
        }
    }

    /**
     * Gets the list of squares on the board.
     *
     * @return a list of squares
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     * Gets the total number of squares on the board.
     *
     * @return the number of squares
     */
    public int amountOfSquares() {
        return squares.size();
    }

    /**
     * Gets the list of pawns on the board.
     *
     * @return a list of pawns
     */
    public List<Pawn> getPawns() {
        return pawns;
    }

    /**
     * Prints the coordinates of all pawns on the board to the console.
     * The coordinates are displayed in a readable format.
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
