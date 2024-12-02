package com.example;


public class Pawn
{
    private int x;
    private int y;
    private String color;

    public Pawn(int x, int y, String color)
    {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getColor()
    {
        return color;
    }

    public void move(int newX, int newY)
    {
        x = newX;
        y = newY;
    }

    public boolean canMove(int newX, int newY, Pawn[][] board)
    {
        if (newX < 0 || newX >= board.length || newY < 0 || newY >= board[0].length)
        {
            return false;
        }

        if(board[newX][newY] != null)
        {
            return false;
        }

        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        //ruch o jedno pole
        if((dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1))
        {
            return true;
        }

        //ruch o dwa pola
        if((dx == 2 && dy == 0) || (dx == 0 && dy == 2) || (dx == 2 && dy == 2))
        {
            int midX = (newX + x) / 2;
            int midY = (newY + y) / 2;

            //sprawdzamy czy pole posrednie umozliwia ruch o 2 pola
            if (midX >= 0 && midY >= 0 && midX < board.length && midY < board[0].length)
            {
                if(board[midX][midY] != null)
                {
                    return true;
                }
            }
        }
        return false;
    }
}