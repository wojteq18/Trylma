package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class GameTest {
    Colors color = Colors.WHITE;
    private FilledBoard filledBoard = new FilledBoard(5, 2);
    private Board board = new Board(5);
    private Board board1 = new Board(7);
    private YinBoard yinBoard = new YinBoard(5, 2);
    private Player player = new Player(color, filledBoard, State.ACTIVE);

    @Test
    public void testPlayerInitialization() {
        assertNotNull(player, "Player instance should not be null.");
        assertEquals(color, player.getColor(), "Player color should match the initialized color.");
        assertEquals(State.ACTIVE, player.getState(), "Player state should be ACTIVE.");
    }

    @Test
    public void testBoardInitialization() {
        assertEquals(121, board.amountOfSquares());
        assertEquals(231, board1.amountOfSquares());
    }

    @Test
    public void testFilledBoardInitialization() {
        assertEquals(5, filledBoard.getLength());
        assertEquals(20, filledBoard.getPawns().size());
    }
}