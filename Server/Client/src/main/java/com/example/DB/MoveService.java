package com.example.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private SaveRepository saveRepository;

    // Dodaj ruch na podstawie nazwy save'a
    public void addMove(String saveName, Integer moveNumber, String moveData) {
        Save save = saveRepository.findBySaveName(saveName); // Znajdź Save po nazwie
        if (save == null) {
            throw new RuntimeException("Save not found with name: " + saveName);
        }

        Move move = new Move();
        move.setSave(save); // Ustaw Save jako klucz obcy
        move.setMoveNumber(moveNumber);
        move.setMoveData(moveData);
        moveRepository.save(move);
    }

    // Pobierz dane ruchu
    public String getMoveData(String saveName, Integer moveNumber) {
        return moveRepository.getMoveData(saveName, moveNumber);
    }
}
