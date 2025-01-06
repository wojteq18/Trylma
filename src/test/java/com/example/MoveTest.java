package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MoveTest {
    Colors color = Colors.WHITE;
    Colors color1 = Colors.BLACK;
    private FilledBoard board = new FilledBoard(5, 2);
    private Player player_white = new Player(color, board, State.ACTIVE);
    private Player player_black = new Player(color1, board, State.ACTIVE);

    @Test
    public void testMoveFunction() {
        assertEquals(player_white.move(3, 3, 4, 4), 0); //ruch jest legalny, wiec funkcja move powinna zwrocic 0
        assertEquals(player_black.move(13, 1, 12, 1), 4); //pole nie istnieje, wiec funkcja move powinna zrocic 4
    }
}