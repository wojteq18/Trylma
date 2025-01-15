package com.example;

import java.util.ArrayList;
import java.util.List;

public class Player{
    
    private Colors color;
    private Board board;
    private List <Pawn> mypawns;
    private State state;

    public Player(Colors color, Board board, State state){
        this.color = color;
        this.board = board;
        this.state = state;
        setPawns();
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public Colors getColor(){
        return color;
    }
    public void setPawns(){
        mypawns = new ArrayList<>();
        for (Pawn pawn : board.getPawns()){
            if (pawn.getColor() == color){
                mypawns.add(pawn);
            }
        }
    }
    public List<Pawn> getpawns(){
        return mypawns;
    }
    public boolean isSquareThere (int x, int y){
        for (Square square : board.getSquares()){
            if (square.getX() == x && square.getY() == y){
                return true;
            }
        }
        return false;
    }
    public boolean isPawnThere(int x, int y){
        for (Pawn pawn : mypawns){
            if (pawn.getX() == x && pawn.getY() == y){
                return true;
            }
        }
        return false;
    }
    public boolean isSquareEmpty(int x, int y){
        for (Square square : board.getSquares()){
            if (square.getX() == x && square.getY() == y){
                if(square.getStatus()){
                    return true;
                } else return false;
            } 
        }
        return true; //to sie nigdy nie powinno wykonać
    }
    public void setSquareStatus(int x, int y, boolean isEmpty){
        for (Square square : board.getSquares()){
            if (square.getX() == x && square.getY() == y){
                square.setStatus(isEmpty);
            }
        }
    }
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
            (startSquare.getColor() == Colors.BLUE && movingPawn.getColor() == Colors.RED)   ||
            (startSquare.getColor() == Colors.RED && movingPawn.getColor() == Colors.BLUE))
        {
            Square targetSquare = null;
            for (Square sq : board.getSquares()) {
                if (sq.getX() == newX && sq.getY() == newY) {
                    targetSquare = sq;
                    break;
                }
            }
            if (targetSquare != null) {
                if (targetSquare.getColor() == startSquare.getColor()) {
                    // Pozostaje w tej samej strefie
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    public int move (int x, int y, int newX, int newY){
        //boolean doneMove = false;
        //while(!doneMove){
            if(isPawnThere(x, y)){
                if (isSquareThere(newX, newY)){
                    if (!(isPawnThere(newX, newY))){
                        if(doesNotEscape(x, y, newX, newY)){
                            Pawn pawn = thisPawn(x, y);
                            multiMove(x, y);
                            oneMove(x, y);
    
                            if (pawn.checkMove(new Move(newX, newY))){
                                pawn.clearMoves();
                                pawn.setX(newX);
                                pawn.setY(newY);
                                setSquareStatus(x, y, true);
                                setSquareStatus(newX, newY, false);
                                
                                return 0; //zwraca 0, gdy ruch jest legalny
                            } else {
                                return 1; //zwraca 1, gdy ruch jest zakazany (?)
                            }
                        } else {
                            return 2; //zwraca 2, gdy probujemy uciec ze strefy finalnej
                        }
                    } else {
                        return 3; //zwraca 3, gdy probujemy wejsc na zajete pole
                    }
                } else {
                    return 4; //zwraca 4, gdy pole nie istnieje
                }
            } else {
                return 5; //zwraca 5, gdy na wskazanym polu nie ma pionka
            }
            
        //}
    }
    
    public void multiMove(int x, int y){
        Pawn pawn = thisPawn(x, y);
        if (pawn == null) {
            return; 
        }
        if((isSquareThere(x, y - 2) && isSquareThere(x, y - 4) && isSquareEmpty(x, y - 4) &&(!isSquareEmpty(x, y - 2)) && doesNotEscape(x, y, x, y - 4))){
            if (!(pawn.checkMove(new Move(x, y - 4)))){
                pawn.addMove(new Move(x, y - 4));
                //multiMove(x, y - 4);
            }
        } 
         if(isSquareThere(x, y + 2) && isSquareThere(x, y + 4) && isSquareEmpty(x, y + 4) &&(!isSquareEmpty(x, y + 2)) && doesNotEscape(x, y, x, y + 4)){
            if (!(pawn.checkMove(new Move(x, y + 4)))){
                pawn.addMove(new Move(x, y + 4));
                //multiMove(x, y + 4);
            }
        } 
         if (isSquareThere(x - 1, y - 1) && isSquareThere(x - 2, y - 2) && isSquareEmpty(x - 2, y - 2) &&(!isSquareEmpty(x - 1, y - 1)) && doesNotEscape(x, y, x - 2, y - 2)){
            if (!(pawn.checkMove(new Move(x - 2, y - 2)))){
                pawn.addMove(new Move(x - 2, y - 2));
                //multiMove(x - 2, y - 2);
            }
        } 
         if (isSquareThere(x + 1, y + 1) && isSquareThere(x + 2, y + 2) && isSquareEmpty(x + 2, y + 2) &&(!isSquareEmpty(x + 1, y + 1)) && doesNotEscape(x, y, x + 2, y + 2)){
            if (!(pawn.checkMove(new Move(x + 2, y + 2)))){
                pawn.addMove(new Move(x + 2, y + 2));
                //multiMove(x + 2, y + 2);
            }
        } 
         if (isSquareThere(x - 1, y + 1) && isSquareThere(x - 2, y + 2) && isSquareEmpty(x - 2, y + 2) && (!isSquareEmpty(x - 1, y + 1)) && doesNotEscape(x, y, x - 2, y + 2)){
            if (!(pawn.checkMove(new Move(x - 2, y + 2)))){
                pawn.addMove(new Move(x - 2, y + 2));
                //multiMove(x - 2, y + 2);
            }
        } 
         if (isSquareThere(x + 1, y - 1) && isSquareThere(x + 2, y - 2) && isSquareEmpty(x + 2, y - 2) && (!isSquareEmpty(x + 1, y - 1)) && doesNotEscape(x, y, x + 2, y - 2)){
            if (!(pawn.checkMove(new Move(x + 2, y - 2)))){
                pawn.addMove(new Move(x + 2, y - 2));
                //multiMove(x + 2, y - 2);
            }
        }
    }
    public Pawn thisPawn (int x, int y){
        for (Pawn pawn : mypawns){
            if (pawn.getX() == x && pawn.getY() == y){
                return pawn;
            }
        }
        return null;
    }
    public void oneMove(int x, int y){
        Pawn pawn = thisPawn(x, y);

        if (pawn == null) {
            return; 
        }
        if (isSquareThere(x, y + 2) && isSquareEmpty(x, y + 2) && doesNotEscape(x, y, x, y + 2)){
            pawn.addMove(new Move(x, y + 2));
        }
        if (isSquareThere(x, y - 2) && isSquareEmpty(x, y - 2) && doesNotEscape(x, y, x, y - 2)){
            pawn.addMove(new Move(x, y - 2));
        }
        if (isSquareThere(x - 1, y - 1) && isSquareEmpty(x - 1, y - 1) && doesNotEscape(x, y, x - 1, y - 1)){
            pawn.addMove(new Move(x - 1, y - 1));
        }
        if (isSquareThere(x + 1, y + 1) && isSquareEmpty(x + 1, y + 1) && doesNotEscape(x, y, x + 1, y + 1)){
            pawn.addMove(new Move(x + 1, y + 1));
        }
        if (isSquareThere(x + 1, y - 1) && isSquareEmpty(x + 1, y - 1) && doesNotEscape(x, y, x + 1, y - 1)){
            pawn.addMove(new Move(x + 1, y - 1));
        }
        if (isSquareThere(x - 1, y + 1) && isSquareEmpty(x - 1, y + 1) && doesNotEscape(x, y, x - 1, y + 1)){
            pawn.addMove(new Move(x - 1, y + 1));
        }
    }
    public void hasWon(){
        Colors playerColor = color;
        switch (playerColor) {
            case WHITE:
                if(checkWin(Colors.BLACK)){
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
    public boolean checkWin(Colors color){
        for(Pawn pawn : mypawns){
            int x = pawn.getX();
            int y = pawn.getY();
            for (Square square : board.getSquares()){
                if (square.getX() == x && square.getY() == y){
                    if (!(square.getColor() == color)){
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