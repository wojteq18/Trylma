package com.example;

/**
 * The Square class represents a single square on the game board.
 * Each square has coordinates, a color, and a status indicating whether it is empty.
 */
public class Square {

    /**
     * The y-coordinate of the square.
     */
    private int y;

    /**
     * The x-coordinate of the square.
     */
    private int x;

    /**
     * The color of the square.
     */
    private Colors color;

    /**
     * Indicates whether the square is empty.
     */
    private boolean isEmpty;

    /**
     * Constructs a Square object with the specified coordinates, color, and empty status.
     *
     * @param x       the x-coordinate of the square
     * @param y       the y-coordinate of the square
     * @param color   the color of the square
     * @param isEmpty the empty status of the square
     */
    public Square(int x, int y, Colors color, boolean isEmpty) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.isEmpty = isEmpty;
    }

    /**
     * Gets the x-coordinate of the square.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the square.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the color of the square.
     *
     * @return the color
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Gets the empty status of the square.
     *
     * @return true if the square is empty, false otherwise
     */
    public boolean getStatus() {
        return isEmpty;
    }

    /**
     * Sets the empty status of the square.
     *
     * @param isEmpty the new empty status
     */
    public void setStatus(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    /**
     * Sets the color of the square.
     *
     * @param color the new color
     */
    public void setColor(Colors color) {
        this.color = color;
    }
}
