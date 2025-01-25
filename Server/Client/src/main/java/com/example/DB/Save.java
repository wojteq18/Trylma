package com.example.DB;

import jakarta.persistence.*;

@Entity
@Table(name = "saves") // Powiązanie z tabelą saves
public class Save {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie klucza głównego
    private Long id;

    @Column(unique = true, nullable = false) // Kolumna unikalna, nie może być null
    private String saveName; // Nazwa save'a, unikalna

    @Column(nullable = false) // Kolumna nie może być null
    private int moveCount; // Ilość ruchów

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }
}
