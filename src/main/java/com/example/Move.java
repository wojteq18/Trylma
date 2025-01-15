package com.example;

import java.util.Objects;

/**
 * The Move class represents a move in the game, defined by x and y coordinates.
 * It includes methods for retrieving the coordinates and comparing moves.
 */
public class Move {

    /**
     * The x-coordinate of the move.
     */
    private int x;

    /**
     * The y-coordinate of the move.
     */
    private int y;

    /**
     * Constructs a Move object with the specified x and y coordinates.
     *
     * @param x the x-coordinate of the move
     * @param y the y-coordinate of the move
     */
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the move.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the move.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Compares this move to another object for equality.
     * Two moves are equal if their x and y coordinates are the same.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return x == move.x && y == move.y;
    }

    /**
     * Generates a hash code for the move based on its x and y coordinates.
     *
     * @return the hash code of the move
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
