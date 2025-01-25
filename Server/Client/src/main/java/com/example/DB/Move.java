package com.example.DB;

import jakarta.persistence.*;

@Entity
@Table(name = "moves") // Powiązanie z tabelą moves
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie klucza głównego
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Powiązanie z tabelą saves
    @JoinColumn(name = "save_id", nullable = false) // Klucz obcy do tabeli saves
    private Save save;

    @Column(name = "move_number", nullable = false) // Kolumna numeru ruchu
    private Integer moveNumber;

    @Column(name = "move_data", nullable = false, columnDefinition = "TEXT") // Kolumna danych ruchu
    private String moveData;

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Save getSave() {
        return save;
    }

    public void setSave(Save save) {
        this.save = save;
    }

    public Integer getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getMoveData() {
        return moveData;
    }

    public void setMoveData(String moveData) {
        this.moveData = moveData;
    }
}
