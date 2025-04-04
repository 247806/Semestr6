package extraction;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class KeyWordsLoader {
    private static Set<String> loadWords(String filePath) {
        Set<String> cities = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    cities.add(line.trim()); // Usuwa zbędne spacje i dodaje miasto do zbioru
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static Set<String> getCities() {
        return loadWords("src/main/resources/keywords/places.txt");
    }

    public static Set<String> getNames() {
        return loadWords("src/main/resources/keywords/names.txt");
    }

    public static Set<String> getCurrencies() {
        return loadWords("src/main/resources/keywords/currency.txt");
    }

    public static Set<String> allKeyWords() {
        Set<String> keyWords = new HashSet<>();

        keyWords.addAll(getCities());
        keyWords.addAll(getNames());
        keyWords.addAll(getCurrencies());

        return keyWords;
    }
}

