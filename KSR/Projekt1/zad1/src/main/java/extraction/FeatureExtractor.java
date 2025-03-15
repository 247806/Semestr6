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

        // 2. Dominująca waluta
        String dominantCurrency = findDominantEntity(text, currencies);
        features.put("dominant_currency", dominantCurrency);

        // 3. Nazwy miast
        List<String> foundCities = findEntities(text, cities);
        features.put("cities", foundCities);

        // 4. Liczba unikalnych słów
        String[] wordsWithoutDots = text.replaceAll("[.,!?:;]", "").split("\\s+");
        Set<String> uniqueWords = new HashSet<>(Arrays.stream(wordsWithoutDots).filter(w -> w.length() >= 3).toList());
        features.put("unique_word_count", uniqueWords.size());

        // 5. Średnia długość słowa
        double avgWordLength = Arrays.stream(wordsWithoutDots).mapToInt(String::length).average().orElse(0);
        features.put("avg_word_length", avgWordLength);

        // 6. Liczba słów kluczowych w pierwszych 3 zdaniach
        String[] sentences = text.split("[.!?]\\s+");
        int firstThreeSentences = getFirstNSentences(sentences, 3, keywords);
        features.put("keywords_in_first_3_sentences", firstThreeSentences);

        // 7. Liczba słów zaczynających się wielką literą (nie licząc początku zdania)
        int capitalizedWordCount = countCapitalizedWords(sentences);
        features.put("capitalized_word_count", capitalizedWordCount);

        // 8. Pierwsze słowo kluczowe w tekście
        String firstKeyword = findFirstKeyword(text, keywords);
        features.put("first_keyword", firstKeyword);

        // 9. Liczba słów kluczowych
        int keywordCount = countKeywordOccurrences(text, keywords);
        features.put("keyword_count", keywordCount);

        // 10. Względna liczba słów kluczowych
        double relativeKeywordCount = keywordCount > 0 ? (double) keywordCount / wordCount : 0;
        features.put("relative_keyword_count", relativeKeywordCount);

        // 11. Nazwiska
        List<String> foundNames = findEntities(text, names);
        features.put("names", foundNames);

        return features;
    }

    private String findDominantEntity(String text, Set<String> entities) {
        Map<String, Integer> frequency = new HashMap<>();
        Map<String, Integer> firstPosition = new HashMap<>();

        for (String entity : entities) {
            int count = countOccurrences(text, entity);
            if (count > 0) {
                frequency.put(entity, count);
                firstPosition.put(entity, text.indexOf(entity));
            }
        }

        return frequency.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = e2.getValue().compareTo(e1.getValue()); // Najpierw sortowanie po liczbie wystąpień (malejąco)
                    if (cmp == 0) { // Jeśli liczby są równe, sortujemy po pierwszej pozycji w tekście (rosnąco)
                        return Integer.compare(firstPosition.get(e1.getKey()), firstPosition.get(e2.getKey()));
                    }
                    return cmp;
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("None");
    }

    private int countOccurrences(String text, String word) {
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    private List<String> findEntities(String text, Set<String> entities) {
        List<String> found = new ArrayList<>();
        for (String entity : entities) {
            int count = countOccurrences(text, entity);
            for (int i = 0; i < count; i++) {
                found.add(entity);
            }
        }
        return found;
    }

    private int getFirstNSentences(String[] text, int n, Set<String> keywords) {
        int count = 0;
        int sentences = n;
        if (text.length < 3) {
            sentences = text.length;
            System.out.println(sentences);
        }
        for (int i = 0; i < sentences; i++) {
            for (String key : keywords) {
                Matcher matcher = Pattern.compile("\\b" + Pattern.quote(key) + "\\b", Pattern.CASE_INSENSITIVE).matcher(text[i]);
                if (matcher.find()) {
                    count+=1;
                }
            }
        }
        return count;
    }

    private int countKeywordOccurrences(String text, Set<String> keywords) {
        String[] words = text.split("\\s+");
        int count = 0;
        for (String key : keywords) {
            int tempCount = countOccurrences(text, key);
            count += tempCount;
        }
        return count;
    }

    private int countCapitalizedWords(String[] sentences) {
        int count = 0;
        for (String sentence : sentences) {
            String[] words = sentence.split("\\s+");
            for (int i = 1; i < words.length; i++) {
                if (Character.isUpperCase(words[i].charAt(0))) {
                count++;
                }
            }
        }
        return count;
    }

    private String findFirstKeyword(String text, Set<String> keywords) {
        String found;
        for (String key : keywords) {
            if (text.contains(key)) {
                found = key;
                return found;
            }
        }
        return "None";
    }

}

