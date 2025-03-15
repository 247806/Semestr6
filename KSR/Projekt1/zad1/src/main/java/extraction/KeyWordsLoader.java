package extraction;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class KeyWordsLoader {
    public static Set<String> loadWords(String filePath) {
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

    public static Set<String> allKeyWords(List<String> filePaths) {
        Set<String> keyWords = new HashSet<>();
        try {
            for (String filePath : filePaths) {
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        keyWords.add(line.trim()); // Usuwa zbędne spacje i dodaje miasto do zbioru
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyWords;
    }

    public static void main(String[] args) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("src/main/resources/keywords/places.txt");
        filePaths.add("src/main/resources/keywords/currency.txt");
        filePaths.add("src/main/resources/keywords/names.txt");
        Set<String> cities = loadWords(filePaths.get(0));
        Set<String> currencies = loadWords(filePaths.get(1));
        Set<String> names = loadWords(filePaths.get(2));
        Set<String> keyWords = allKeyWords(filePaths);
        // Wyświetl wczytane miasta
        System.out.println("Wczytane miasta: " + cities);
        System.out.println("Wczytane waluty: " + currencies);
        System.out.println("Wczytane nazwiska: " + names);
        System.out.println("Wszystkie klucze: " + keyWords);
    }
}

