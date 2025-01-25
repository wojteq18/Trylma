/*package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.DB.MoveService;
import com.example.DB.SaveService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MoveService moveService;

    @Autowired
    private SaveService saveService;

    @Override
    public void run(String... args) throws Exception {
        // Dodaj Save
        saveService.addSave("TestSave", 4);

        // Dodaj ruch do Save
        moveService.addMove("TestSave", 1, "MoveData1");
        moveService.addMove("TestSave", 2, "MoveData2");

        // Pobierz i wyświetl ruch
        String moveData = moveService.getMoveData("TestSave", 1);
        System.out.println("Retrieved Move Data: " + moveData);
    }
}
*/