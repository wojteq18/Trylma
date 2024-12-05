package com.example.Message;

public class MoveMessage implements Message
{
    private int x;
    private int y;
    private int newX;
    private int newY;
    
    public MoveMessage(int x, int y, int newX, int newY)
    {
        this.x = x;
        this.y = y;
        this.newX = newX;
        this.newY = newY;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }

    public int getNewX()
    {
        return newX;
    }

    public int getNewY()
    {
        return newY;
    }
    
    @Override
    public MessageType getType()
    {
        return MessageType.MOVE;
    }
}