package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

private Board board;
    @BeforeEach
    void setUp() {
        board = new Board(5);
    }

    @Test
    public void testBoardLength() {
        assertEquals(5, board.getLength(), "Długość planszy powinna wynosić 5.");
    }
}
