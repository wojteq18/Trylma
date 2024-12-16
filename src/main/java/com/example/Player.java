package com.example;

import java.util.ArrayList;
import java.util.List;

public class Player{
    
    private Colors color;
    private FilledBoard board;
    private List <Pawn> mypawns;
    private State state;

    public Player(Colors color, FilledBoard board, State state){
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
    public boolean doesNotEscape (int x, int y, int newX, int newY){
        for (Square square : board.getSquares()){
            if (square.getX() == x && square.getY() == y){
                for (Pawn pawn : mypawns){
                    if (pawn.getX() == x && pawn.getY() == y){
                        if ((square.getColor() == Colors.BLACK && pawn.getColor() == Colors.WHITE) ||
                        (square.getColor() == Colors.WHITE && pawn.getColor() == Colors.BLACK) ||
                        (square.getColor() == Colors.YELLOW && pawn.getColor() == Colors.GREEN) ||
                        (square.getColor() == Colors.GREEN && pawn.getColor() == Colors.YELLOW) ||
                        (square.getColor() == Colors.BLUE && pawn.getColor() == Colors.RED) ||
                        (square.getColor() == Colors.RED && pawn.getColor() == Colors.BLUE)){
                            for (Square newsquare : board.getSquares()){
                                if (newsquare.getX() == newX && newsquare.getY() == newY){
                                    if(newsquare.getColor() == square.getColor()){
                                        return true;
                                    } else return false;
                                } //else return false; //nie wykona sie, bo wczesniej sie upewniamy, czy docelowe pole istnieje
                            }
                        }
                    } else return true;
                }
            } //else return false; //nie wykona sie, bo zawsze przed ta funkcja upewniamy się, czy dane pole istnieje
        } return false; //rowniez raczej sie nie wykona
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
    
                            //for (Move move : pawn.getMoves()){
                            //    System.out.println(pawn.getX() + " " + pawn.getY() + " " + move.getX() + " " + move.getY());
                            //}
    
                            if (pawn.checkMove(new Move(newX, newY))){
                                pawn.clearMoves();
                                pawn.setX(newX);
                                pawn.setY(newY);
                                setSquareStatus(x, y, true);
                                setSquareStatus(newX, newY, false);
                                //doneMove = true;
                                //System.out.println("Ok, Move has been done!");
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
    
            /*
                    for (Move move : pawn.getMoves()){
                        System.out.println(pawn.getX() + " " + pawn.getY() + " " + move.getX() + " " + move.getY());
                    }
            */
        //}
    }
    
    public void multiMove(int x, int y){
        Pawn pawn = thisPawn(x, y);
        if((isSquareThere(x, y - 2) && isSquareThere(x, y - 4) && isSquareEmpty(x, y - 4) &&(!isSquareEmpty(x, y - 2)) && doesNotEscape(x, y, x, y - 4))){
            if (!(pawn.checkMove(new Move(x - 4, y)))){
                pawn.addMove(new Move(x - 4, y));
                multiMove(x - 4, y);
            }
        } else if(isSquareThere(x, y + 2) && isSquareThere(x, y + 4) && isSquareEmpty(x, y + 4) &&(!isSquareEmpty(x, y + 2)) && doesNotEscape(x, y, x, y + 4)){
            if (!(pawn.checkMove(new Move(x + 4, y)))){
                pawn.addMove(new Move(x + 4, y));
                multiMove(x + 4, y);
            }
        } else if (isSquareThere(x - 1, y - 1) && isSquareThere(x - 2, y - 2) && isSquareEmpty(x - 2, y - 2) &&(!isSquareEmpty(x - 1, y - 1)) && doesNotEscape(x, y, x - 2, y - 2)){
            if (!(pawn.checkMove(new Move(x - 2, y - 2)))){
                pawn.addMove(new Move(x - 2, y - 2));
                multiMove(x - 2, y - 2);
            }
        } else if (isSquareThere(x + 1, y + 1) && isSquareThere(x + 2, y + 2) && isSquareEmpty(x + 2, y + 2) &&(!isSquareEmpty(x + 1, y + 1)) && doesNotEscape(x, y, x + 2, y + 2)){
            if (!(pawn.checkMove(new Move(x + 2, y + 2)))){
                pawn.addMove(new Move(x + 2, y + 2));
                multiMove(x + 2, y + 2);
            }
        } else if (isSquareThere(x - 1, y + 1) && isSquareThere(x - 2, y + 2) && isSquareEmpty(x - 2, y + 2) && (!isSquareEmpty(x - 1, y + 1)) && doesNotEscape(x, y, x - 2, y + 2)){
            if (!(pawn.checkMove(new Move(x - 2, y + 2)))){
                pawn.addMove(new Move(x - 2, y + 2));
                multiMove(x - 2, y + 2);
            }
        } else if (isSquareThere(x + 1, y - 1) && isSquareThere(x + 2, y - 2) && isSquareEmpty(x + 2, y - 2) && (!isSquareEmpty(x + 1, y - 1)) && doesNotEscape(x, y, x + 2, y - 2)){
            if (!(pawn.checkMove(new Move(x + 2, y - 2)))){
                pawn.addMove(new Move(x + 2, y - 2));
                multiMove(x + 2, y - 2);
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
        if (isSquareThere(x, y + 2) && isSquareEmpty(x, y + 2) && doesNotEscape(x, y, x, y + 2)){
            pawn.addMove(new Move(x, y + 2));
        }
        if (isSquareThere(x, y - 2) && isSquareEmpty(x, y - 2) && doesNotEscape(x, y, x, y - 2)){
            pawn.addMove(new Move(x - 2, y));
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
}