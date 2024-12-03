package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<String, List<int[]>> startCoordinates;
    private Map<String, String> opposingColors;

    public Board() {
        startCoordinates = new HashMap<>();
        opposingColors = new HashMap<>();

        // Ręczne przypisanie współrzędnych startowych
        startCoordinates.put("Red", List.of(
            new int[]{9, 0}, new int[]{8, 1}, new int[]{10, 1}, new int[]{7, 2},
            new int[]{9, 2}, new int[]{11, 2}, new int[]{6, 3}, new int[]{8, 3},
            new int[]{10, 3}, new int[]{12, 3}
        ));
        startCoordinates.put("Blue", List.of(
            new int[]{6, 13}, new int[]{8, 13}, new int[]{10, 13}, new int[]{12, 13},
            new int[]{7, 14}, new int[]{9, 14}, new int[]{11, 14}, new int[]{8, 15},
            new int[]{10, 15}, new int[]{9, 16}
        ));
        startCoordinates.put("Green", List.of(
            new int[]{0, 9}, new int[]{1, 8}, new int[]{1, 10}, new int[]{2, 7},
            new int[]{2, 9}, new int[]{2, 11}, new int[]{3, 6}, new int[]{3, 8},
            new int[]{3, 10}, new int[]{3, 12}
        ));

        // Mapowanie przeciwnych kolorów
        opposingColors.put("Red", "Yellow");
        opposingColors.put("Blue", "Green");
        opposingColors.put("Green", "Blue");
        opposingColors.put("Yellow", "Red");
        opposingColors.put("Purple", "Orange");
        opposingColors.put("Orange", "Purple");
    }

    // Pobranie współrzędnych startowych dla danego koloru
    public List<int[]> getStartCoordinates(String color) {
        return startCoordinates.getOrDefault(color, new ArrayList<>());
    }

    // Wyznaczenie goalArea na podstawie przeciwnych współrzędnych
    public List<int[]> getGoalArea(String color) {
        String opposingColor = opposingColors.get(color);
        if (opposingColor != null) {
            return getStartCoordinates(opposingColor);
        }
        return new ArrayList<>(); // Jeśli brak przeciwnika, zwracamy pustą listę
    }
}