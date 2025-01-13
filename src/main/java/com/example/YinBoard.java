package com.example;

public class YinBoard extends FilledBoard{

    public YinBoard(int length, int numberOfPlayers){
        super(length, numberOfPlayers);
        if (numberOfPlayers != 2){
            throw new IllegalArgumentException("Yin error!");
        }
        yinSwitch();
    }

    public void yinSwitch() {
        // Usuń pionki o kolorze BLACK
        pawns.removeIf(pawn -> pawn.getColor() == Colors.BLACK);

        // Zmień status pól o kolorze BLACK na true
        for (Square square : squares) {
            if (square.getColor() == Colors.BLACK) {
                square.setStatus(true);
            }
        }

        // Dodaj nowe pionki o kolorze GREEN i zmień status pól
        for (Square square : squares) {
            if (square.getColor() == Colors.GREEN) {
                pawns.add(new Pawn(square.getX(), square.getY(), Colors.GREEN));
                square.setStatus(false);
            }
        }
    }

    public void printAllCoordinates() {
        StringBuilder sb = new StringBuilder("[");
        for (Pawn pawn : pawns) {
            sb.append("(").append(pawn.getX()).append(", ").append(pawn.getY()).append("), ");
        }
        if (!pawns.isEmpty()) {
            sb.setLength(sb.length() - 2); // Usuń ostatni przecinek i spację
        }
        sb.append("]");
        System.out.println(sb);
    }
}