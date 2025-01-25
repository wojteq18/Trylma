package com.example.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveService {

    @Autowired
    private SaveRepository saveRepository;

    // Dodawanie save'a z liczbą ruchów
    public void addSave(String saveName, int moveCount) {
        Save save = new Save();
        save.setSaveName(saveName);
        save.setMoveCount(moveCount); // Ustawienie liczby ruchów
        saveRepository.save(save);
    }

    // Pobieranie save'a na podstawie nazwy
    public Save getSave(String saveName) {
        return saveRepository.findBySaveName(saveName);
    }

    // Aktualizacja liczby ruchów dla istniejącego save'a
    public void updateMoveCount(String saveName, int newMoveCount) {
        Save save = saveRepository.findBySaveName(saveName);
        if (save != null) {
            save.setMoveCount(newMoveCount); // Aktualizacja pola moveCount
            saveRepository.save(save); // Zapisanie zmiany
        }
    }
}
