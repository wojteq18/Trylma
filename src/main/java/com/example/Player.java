package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represents a player in the game, managing their pawns,
 * interactions with the board, and game state.
 */
public class Player {

    /**
     * The color of the player's pawns.
     */
    private Colors color;

    /**
     * The game board associated with the player.
     */
    private Board board;

    /**
     * The list of pawns belonging to the player.
     */
    private List<Pawn> mypawns;

    /**
     * The current state of the player.
     */
    private State state;

    /**
     * Constructs a Player with the specified color, board, and state.
     *
     * @param color the color of the player's pawns
     * @param board the game board
     * @param state the initial state of the player
     */
    public Player(Colors color, Board board, State state) {
        this.color = color;
        this.board = board;
        this.state = state;
        setPawns();
    }

    /**
     * Sets the player's state.
     *
     * @param state the new state of the player
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the player's state.
     *
     * @return the state of the player
     */
    public State getState() {
        return state;
    }

    /**
     * Gets the color of the player's pawns.
     *
     * @return the player's color
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Initializes the player's pawns based on the board and player's color.
     */
    public void setPawns() {
        mypawns = new ArrayList<>();
        for (Pawn pawn : board.getPawns()) {
            if (pawn.getColor() == color) {
                mypawns.add(pawn);
            }
        }
    }

    /**
     * Gets the player's pawns.
     *
     * @return a list of the player's pawns
     */
    public List<Pawn> getpawns() {
        return mypawns;
    }

    /**
     * Checks if a square with the specified coordinates exists on the board.
     *
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @return true if the square exists, false otherwise
     */
    public boolean isSquareThere(int x, int y) {
        for (Square square : board.getSquares()) {
            if (square.getX() == x && square.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a pawn exists at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if a pawn is present, false otherwise
     */
    public boolean isPawnThere(int x, int y) {
        for (Pawn pawn : mypawns) {
            if (pawn.getX() == x && pawn.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a square at the specified coordinates is empty.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the square is empty, false otherwise
     */
    public boolean isSquareEmpty(int x, int y) {
        for (Square square : board.getSquares()) {
            if (square.getX() == x && square.getY() == y) {
                if (square.getStatus()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true; // This should never execute
    }

    /**
     * Updates the status of a square at the specified coordinates.
     *
     * @param x       the x-coordinate of the square
     * @param y       the y-coordinate of the square
     * @param isEmpty the new status of the square
     */
    public void setSquareStatus(int x, int y, boolean isEmpty) {
        for (Square square : board.getSquares()) {
            if (square.getX() == x && square.getY() == y) {
                square.setStatus(isEmpty);
            }
        }
    }

    /**
     * Verifies if a pawn's movement does not escape its designated area.
     *
     * @param x     the current x-coordinate
     * @param y     the current y-coordinate
     * @param newX  the target x-coordinate
     * @param newY  the target y-coordinate
     * @return true if the move is valid within the area, false otherwise
     */
    public boolean doesNotEscape(int x, int y, int newX, int newY) {
        Square startSquare = null;
        for (Square sq : board.getSquares()) {
            if (sq.getX() == x && sq.getY() == y) {
                startSquare = sq;
                break;
            }
        }
        if (startSquare == null) {
            return true;
        }
        Pawn movingPawn = null;
        for (Pawn p : mypawns) {
            if (p.getX() == x && p.getY() == y) {
                movingPawn = p;
                break;
            }
        }
        if (movingPawn == null) {
            return true;
        }

        if ((startSquare.getColor() == Colors.BLACK && movingPawn.getColor() == Colors.WHITE) ||
            (startSquare.getColor() == Colors.WHITE && movingPawn.getColor() == Colors.BLACK) ||
            (startSquare.getColor() == Colors.YELLOW && movingPawn.getColor() == Colors.GREEN) ||
            (startSquare.getColor() == Colors.GREEN && movingPawn.getColor() == Colors.YELLOW) ||
            (startSquare.getColor() == Colors.BLUE && movingPawn.getColor() == Colors.RED) ||
            (startSquare.getColor() == Colors.RED && movingPawn.getColor() == Colors.BLUE)) {
            Square targetSquare = null;
            for (Square sq : board.getSquares()) {
                if (sq.getX() == newX && sq.getY() == newY) {
                    targetSquare = sq;
                    break;
                }
            }
            if (targetSquare != null) {
                if (targetSquare.getColor() == startSquare.getColor()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Moves a pawn from the current position to a new position if the move is valid.
     *
     * @param x     the current x-coordinate
     * @param y     the current y-coordinate
     * @param newX  the target x-coordinate
     * @param newY  the target y-coordinate
     * @return an integer indicating the result of the move (0 for success, other values for errors)
     */
    public int move(int x, int y, int newX, int newY) {
        if (isPawnThere(x, y)) {
            if (isSquareThere(newX, newY)) {
                if (!isPawnThere(newX, newY)) {
                    if (doesNotEscape(x, y, newX, newY)) {
                        Pawn pawn = thisPawn(x, y);
                        multiMove(x, y);
                        oneMove(x, y);

                        if (pawn.checkMove(new Move(newX, newY))) {
                            pawn.clearMoves();
                            pawn.setX(newX);
                            pawn.setY(newY);
                            setSquareStatus(x, y, true);
                            setSquareStatus(newX, newY, false);

                            return 0; // Returns 0 for a valid move
                        } else {
                            return 1; // Returns 1 for an invalid move
                        }
                    } else {
                        return 2; // Returns 2 for attempting to leave the final zone
                    }
                } else {
                    return 3; // Returns 3 for attempting to move to an occupied square
                }
            } else {
                return 4; // Returns 4 if the target square does not exist
            }
        } else {
            return 5; // Returns 5 if no pawn is present at the starting position
        }
    }

    /**
     * Adds all valid multi-move options for a pawn starting at a given position.
     *
     * @param x the x-coordinate of the pawn
     * @param y the y-coordinate of the pawn
     */
    public void multiMove(int x, int y) {
        Pawn pawn = thisPawn(x, y);
        if (pawn == null) {
            return;
        }
        if (isSquareThere(x, y - 2) && isSquareThere(x, y - 4) && isSquareEmpty(x, y - 4) && (!isSquareEmpty(x, y - 2)) && doesNotEscape(x, y, x, y - 4)) {
            if (!(pawn.checkMove(new Move(x, y - 4)))) {
                pawn.addMove(new Move(x, y - 4));
            }
        }
        if (isSquareThere(x, y + 2) && isSquareThere(x, y + 4) && isSquareEmpty(x, y + 4) && (!isSquareEmpty(x, y + 2)) && doesNotEscape(x, y, x, y + 4)) {
            if (!(pawn.checkMove(new Move(x, y + 4)))) {
                pawn.addMove(new Move(x, y + 4));
            }
        }
        if (isSquareThere(x - 1, y - 1) && isSquareThere(x - 2, y - 2) && isSquareEmpty(x - 2, y - 2) && (!isSquareEmpty(x - 1, y - 1)) && doesNotEscape(x, y, x - 2, y - 2)) {
            if (!(pawn.checkMove(new Move(x - 2, y - 2)))) {
                pawn.addMove(new Move(x - 2, y - 2));
            }
        }
        if (isSquareThere(x + 1, y + 1) && isSquareThere(x + 2, y + 2) && isSquareEmpty(x + 2, y + 2) && (!isSquareEmpty(x + 1, y + 1)) && doesNotEscape(x, y, x + 2, y + 2)) {
            if (!(pawn.checkMove(new Move(x + 2, y + 2)))) {
                pawn.addMove(new Move(x + 2, y + 2));
            }
        }
        if (isSquareThere(x - 1, y + 1) && isSquareThere(x - 2, y + 2) && isSquareEmpty(x - 2, y + 2) && (!isSquareEmpty(x - 1, y + 1)) && doesNotEscape(x, y, x - 2, y + 2)) {
            if (!(pawn.checkMove(new Move(x - 2, y + 2)))) {
                pawn.addMove(new Move(x - 2, y + 2));
            }
        }
        if (isSquareThere(x + 1, y - 1) && isSquareThere(x + 2, y - 2) && isSquareEmpty(x + 2, y - 2) && (!isSquareEmpty(x + 1, y - 1)) && doesNotEscape(x, y, x + 2, y - 2)) {
            if (!(pawn.checkMove(new Move(x + 2, y - 2)))) {
                pawn.addMove(new Move(x + 2, y - 2));
            }
        }
    }

    /**
     * Retrieves the pawn at a specific position.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @return the pawn at the position, or null if none exists
     */
    public Pawn thisPawn(int x, int y) {
        for (Pawn pawn : mypawns) {
            if (pawn.getX() == x && pawn.getY() == y) {
                return pawn;
            }
        }
        return null;
    }

    /**
     * Adds all valid single-move options for a pawn starting at a given position.
     *
     * @param x the x-coordinate of the pawn
     * @param y the y-coordinate of the pawn
     */
    public void oneMove(int x, int y) {
        Pawn pawn = thisPawn(x, y);
        if (pawn == null) {
            return;
        }
        if (isSquareThere(x, y + 2) && isSquareEmpty(x, y + 2) && doesNotEscape(x, y, x, y + 2)) {
            pawn.addMove(new Move(x, y + 2));
        }
        if (isSquareThere(x, y - 2) && isSquareEmpty(x, y - 2) && doesNotEscape(x, y, x, y - 2)) {
            pawn.addMove(new Move(x, y - 2));
        }
        if (isSquareThere(x - 1, y - 1) && isSquareEmpty(x - 1, y - 1) && doesNotEscape(x, y, x - 1, y - 1)) {
            pawn.addMove(new Move(x - 1, y - 1));
        }
        if (isSquareThere(x + 1, y + 1) && isSquareEmpty(x + 1, y + 1) && doesNotEscape(x, y, x + 1, y + 1)) {
            pawn.addMove(new Move(x + 1, y + 1));
        }
        if (isSquareThere(x + 1, y - 1) && isSquareEmpty(x + 1, y - 1) && doesNotEscape(x, y, x + 1, y - 1)) {
            pawn.addMove(new Move(x + 1, y - 1));
        }
        if (isSquareThere(x - 1, y + 1) && isSquareEmpty(x - 1, y + 1) && doesNotEscape(x, y, x - 1, y + 1)) {
            pawn.addMove(new Move(x - 1, y + 1));
        }
    }

    /**
     * Checks if the player has won the game by verifying the positions of all pawns.
     */
    public void hasWon() {
        Colors playerColor = color;
        switch (playerColor) {
            case WHITE:
                if (checkWin(Colors.BLACK)) {
                    state = State.INACTIVE;
                }
                break;
            case BLACK:
                if (checkWin(Colors.WHITE)) {
                    state = State.INACTIVE;
                }
                break;
            case YELLOW:
                if (checkWin(Colors.RED)) {
                    state = State.INACTIVE;
                }
                break;
            case RED:
                if (checkWin(Colors.YELLOW)) {
                    state = State.INACTIVE;
                }
                break;
            case BLUE:
                if (checkWin(Colors.GREEN)) {
                    state = State.INACTIVE;
                }
                break;
            case GREEN:
                if (checkWin(Colors.BLUE)) {
                    state = State.INACTIVE;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Verifies if all pawns are in the correct area to declare victory.
     *
     * @param color the target color of the victory area
     * @return true if all pawns are in the correct area, false otherwise
     */
    public boolean checkWin(Colors color) {
        for (Pawn pawn : mypawns) {
            int x = pawn.getX();
            int y = pawn.getY();
            for (Square square : board.getSquares()) {
                if (square.getX() == x && square.getY() == y) {
                    if (!(square.getColor() == color)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //funkcja testowa, dla sprawdzenia poprawności metody hasWon()
    public void hasWonTest(){
        Colors playerColor = color;
        switch (playerColor) {
            case WHITE:
                if(checkWin(Colors.WHITE)){
                    state = State.INACTIVE;
                }
                break;
            case BLACK:
                if(checkWin(Colors.WHITE)){
                    state = State.INACTIVE;
                }
                break;
            case YELLOW:
                if(checkWin(Colors.RED)){
                    state = State.INACTIVE;
                }
                break;
            case RED:
                if(checkWin(Colors.YELLOW)){
                    state = State.INACTIVE;
                }
                break;
            case BLUE:
                if(checkWin(Colors.GREEN)){
                    state = State.INACTIVE;
                }
                break;
            case GREEN:
                if(checkWin(Colors.BLUE)){
                    state = State.INACTIVE;
                }
                break;
            default:
                break;
        }
    }
}
