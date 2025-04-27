package ksr.extraction;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class StopListFilter {

    public static Set<String> loadStopList(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            System.err.println("Error reading stop list file: " + e.getMessage());
            throw e;
        }
    }

    public static String removeStopWords(String text, Set<String> stopList) {
        String[] words = text.split("\\s+");
        return Arrays.stream(words)
                .filter(word -> !stopList.contains(word.toLowerCase()))
                .collect(Collectors.joining(" "));
    }

}
