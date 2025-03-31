package ksr.extraction;

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
}

