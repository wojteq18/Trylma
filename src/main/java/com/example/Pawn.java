package com.example;

import java.util.HashSet;
import java.util.Set;

/**
 * The Pawn class represents a pawn in the game.
 * A pawn has coordinates, a color, and a set of possible moves.
 */
public class Pawn {

    /**
     * The x-coordinate of the pawn.
     */
    private int x;

    /**
     * The y-coordinate of the pawn.
     */
    private int y;

    /**
     * The color of the pawn.
     */
    private Colors color;

    /**
     * The set of possible moves for the pawn.
     */
    private Set<Move> moves = new HashSet<>();

    /**
     * Constructs a Pawn with the specified coordinates and color.
     *
     * @param x     the x-coordinate of the pawn
     * @param y     the y-coordinate of the pawn
     * @param color the color of the pawn
     */
    public Pawn(int x, int y, Colors color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Gets the x-coordinate of the pawn.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the pawn.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the color of the pawn.
     *
     * @return the color of the pawn
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Sets the x-coordinate of the pawn.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the pawn.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Adds a possible move to the pawn's move set.
     *
     * @param move the move to add
     */
    public void addMove(Move move) {
        moves.add(move);
    }

    /**
     * Removes a move from the pawn's move set.
     *
     * @param move the move to remove
     */
    public void removeMove(Move move) {
        moves.remove(move);
    }

    /**
     * Checks if a given move is valid for the pawn.
     *
     * @param move the move to check
     * @return true if the move is valid, false otherwise
     */
    public boolean checkMove(Move move) {
        return moves.contains(move);
    }

    /**
     * Clears all possible moves for the pawn.
     */
    public void clearMoves() {
        moves.clear();
    }

    /**
     * Gets the set of possible moves for the pawn.
     *
     * @return the set of moves
     */
    public Set<Move> getMoves() {
        return moves;
    }
}
