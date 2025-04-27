package ksr.extraction;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;


public class KeyWordsLoader {
    private static Set<String> loadWords(String filePath) {
        Set<String> cities = new HashSet<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(cities::add);
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

