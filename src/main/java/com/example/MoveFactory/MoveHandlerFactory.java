package com.example.MoveFactory;

public class MoveHandlerFactory {
    public static MoveHandler getHandler (int moveResult, int numberOfPlayers, int queue) {
        switch (moveResult) {
            case 0:
                return new OkMoveHandler(numberOfPlayers, queue);
            case 1:
                return new ForbiddenMove(numberOfPlayers, queue);
            case 2:
                return new EscapeMove(numberOfPlayers, queue);
            case 3:
                return new NotEmptyMove(numberOfPlayers, queue);
            case 4:
                return new NotExistMove(numberOfPlayers, queue);
            case 5:
                return new NoPawnMove(numberOfPlayers, queue); 
            default:
                return new UnknownError(numberOfPlayers, queue);                        
        }
    }
}