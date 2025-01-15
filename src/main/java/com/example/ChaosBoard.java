package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The ChaosBoard class represents a variant of the Board with chaotic configurations.
 * It includes additional features like setting pawns randomly and swapping square colors.
 */
public class ChaosBoard extends Board {

    /**
     * Constructs a ChaosBoard with the specified length and number of players.
     * Initializes the chaotic configuration of the board.
     *
     * @param length         the length of the board
     * @param numberOfPlayers the number of players in the game
     */
    public ChaosBoard(int length, int numberOfPlayers) {
        super(length);
        createChaos(numberOfPlayers);
    }

    /**
     * Creates a chaotic configuration for the board based on the number of players.
     * Pawns are set and square colors are swapped accordingly.
     *
     * @param numberOfPlayers the number of players in the game
     */
    public void createChaos(int numberOfPlayers) {
        pawns = new ArrayList<>();
        switch (numberOfPlayers) {
            case 2:
                setPawn(Colors.BLACK);
                setPawn(Colors.WHITE);
                break;
            case 3:
                setPawn(Colors.BLACK);
                setPawn(Colors.YELLOW);
                setPawn(Colors.RED);
                break;
            case 4:
                setPawn(Colors.BLUE);
                setPawn(Colors.YELLOW);
                setPawn(Colors.RED);
                setPawn(Colors.GREEN);
                break;
            case 6:
                setPawn(Colors.BLACK);
                setPawn(Colors.YELLOW);
                setPawn(Colors.WHITE);
                setPawn(Colors.GREEN);
                setPawn(Colors.RED);
                setPawn(Colors.BLUE);
                break;
            default:
                System.err.println("Wrong amount of players!");
                break;
        }
        changeSquareColor(Colors.BLACK, Colors.WHITE);
        changeSquareColor(Colors.WHITE, Colors.BLACK);
        changeSquareColor(Colors.BLUE, Colors.YELLOW);
        changeSquareColor(Colors.YELLOW, Colors.BLUE);
        changeSquareColor(Colors.RED, Colors.GREEN);
        changeSquareColor(Colors.GREEN, Colors.RED);
    }

    /**
     * Places pawns of the specified color randomly on the board.
     * Only squares with the color Colors.NULL and status true are considered.
     *
     * @param color the color of the pawns to set
     */
    public void setPawn(Colors color) {
        List<Square> chaosSquares = new ArrayList<>();
        for (Square square : squares) {
            if (square.getColor() == Colors.NULL && square.getStatus()) {
                chaosSquares.add(square);
            }
        }
        Random rand = new Random();
        for (int i = 1; i <= 10; i++) {
            int randomIndex = rand.nextInt(chaosSquares.size());
            Square square = chaosSquares.get(randomIndex);
            pawns.add(new Pawn(square.getX(), square.getY(), color));
            square.setStatus(false);
        }
    }

    /**
     * Changes the color of all squares from the start color to the end color.
     *
     * @param start the color to be replaced
     * @param end   the new color to assign
     */
    public void changeSquareColor(Colors start, Colors end) {
        for (Square square : squares) {
            if (square.getColor() == start) {
                square.setColor(end);
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
