package com.example.Message;

public class JoinMessage implements Message
{
    private int clientCount;
    public JoinMessage(int clientCount)
    {
        this.clientCount = clientCount;
    }

    public int getClientCount()
    {
        return clientCount;
    }

    @Override
    public MessageType getType()
    {
        return MessageType.JOIN;
    }
}