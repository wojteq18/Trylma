package com.example.Message;

public class BuilderMessage {
    private String error;
    private int x;
    private int y;
    private int newX;
    private int newY;
    private MessageType type;
    private int clientCount;

    public BuilderMessage(MessageType type) {
        this.type = type;
    }

    public BuilderMessage error(String error) {
        this.error = error;
        return this;
    }

    public BuilderMessage from(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public BuilderMessage to(int newX, int newY) {
        this.newX = newX;
        this.newY = newY;
        return this;
    }

    public BuilderMessage clientCount(int clientCount) {
        this.clientCount = clientCount;
        return this;
    }

    public Message build() {
        switch (type) {
            case ERROR:
                return new ErrorMessage(error);
            case MOVE:
                return new MoveMessage(x, y, newX, newY);
            case JOIN:
                return new JoinMessage(clientCount);
            default:
                throw new IllegalArgumentException("Unknown message type: " + type);
        }
    }
}