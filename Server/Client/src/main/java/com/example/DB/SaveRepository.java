package com.example.DB;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveRepository extends JpaRepository<Save, Long> {
    Save findBySaveName(String saveName); // Pobierz dane na podstawie saveName
}
