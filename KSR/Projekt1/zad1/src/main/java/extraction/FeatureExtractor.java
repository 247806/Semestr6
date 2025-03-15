package extraction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.*;
import java.util.regex.*;

import static extraction.KeyWordsLoader.allKeyWords;
import static extraction.KeyWordsLoader.loadWords;

@Getter
public class FeatureExtractor {
    private final List<String> filePaths;
    private final Set<String> cities;
    private final Set<String> currencies;
    private final Set<String> names;
    private final Set<String> keywords;

    public FeatureExtractor(List<String> filePaths) {
        this.filePaths = filePaths;
        cities = loadWords(filePaths.get(0));
        currencies = loadWords(filePaths.get(1));
        names = loadWords(filePaths.get(2));
        keywords = allKeyWords(filePaths);
    }

    public Map<String, Object> extractFeatures(String text) {
        Map<String, Object> features = new HashMap<>();
//        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // 1. Długość tekstu (liczba słów o długości >= 3)
        String[] words = text.split("\\s+");

        int wordCount = (int) Arrays.stream(words).filter(w -> w.length() >= 3).count();
        features.put("length", wordCount);

        // 3. Nazwy miast
        Set<String> foundCities = findEntities(text, cities);
        features.put("cities", foundCities);

        // 4. Liczba unikalnych słów
        String[] wordsWithoutDots = text.replaceAll("[.,!?:;]", "").split("\\s+");
        Set<String> uniqueWords = new HashSet<>(Arrays.stream(wordsWithoutDots).filter(w -> w.length() >= 3).toList());
        features.put("unique_word_count", uniqueWords.size());

        // 5. Średnia długość słowa
        double avgWordLength = Arrays.stream(wordsWithoutDots).mapToInt(String::length).average().orElse(0);
        features.put("avg_word_length", avgWordLength);

        return features;
    }

    private Set<String> findEntities(String text, Set<String> entities) {
        Set<String> found = new HashSet<>();
        for (String entity : entities) {
            if (text.contains(entity)) {
                found.add(entity);
            }
        }
        return found;
    }

//    public static void main(String[] args) {
//        String sampleText = "AMR Corp will hold a press conference this morning in New York at 0900 EST. The U.S. Dollar gained strength against the Japanese Yen. Reagan was quoted as saying the administration supports economic growth.";
//        Map<String, Object> features = extractFeatures(sampleText);
//        features.forEach((k, v) -> System.out.println(k + ": " + v));
//    }
}

