package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GameStatusTest {
    Colors color_white = Colors.WHITE;
    Colors color_black = Colors.BLACK;
    private FilledBoard filledBoard = new FilledBoard(5, 2);
    private Player player_black = new Player(color_black, filledBoard, State.ACTIVE);
    private Player player_white = new Player(color_white, filledBoard, State.ACTIVE);

    @Test
    public void testPlayerStatusLost() {
        assertEquals(player_black.checkWin(color_white), false);
    }

    @Test
    public void testPlayerStatusWin() {
        assertEquals(player_white.checkWin(color_white), true); 
        /*przwdziwą sytuację odzwierciedla test, w którym dla gracza białego sprawdzamy czy wygrał, ale funkcja 
        check win przyjmuje color_black - jednak tylko dla sprawdzenia poprawności funkcji sprawdzających wygraną 
        sprawdzamy dla koloru białego*/
        player_white.hasWonTest();
        assertEquals(player_white.getState(), State.INACTIVE);
    }
}