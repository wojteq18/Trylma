package com.example.MoveFactory;

public class MoveHandlerFactory {
    public static MoveHandler getHandler (int moveResult) {
        switch (moveResult) {
            case 0:
                return new OkMoveHandler();
            case 1:
                return new ForbiddenMove();
            case 2:
                return new EscapeMove();
            case 3:
                return new NotEmptyMove();
            case 4:
                return new NotExistMove();
            case 5:
                return new NoPawnMove(); 
            default:
                return new UnknownError();                        
        }
    }
}