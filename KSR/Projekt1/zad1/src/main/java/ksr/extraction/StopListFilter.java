package ksr.extraction;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class StopListFilter {

    public static Set<String> loadStopList(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public static String removeStopWords(String text, Set<String> stopList) {
        String[] words = text.split("\\s+");
        return Arrays.stream(words)
                .filter(word -> !stopList.contains(word.toLowerCase()))
                .collect(Collectors.joining(" "));
    }

}
