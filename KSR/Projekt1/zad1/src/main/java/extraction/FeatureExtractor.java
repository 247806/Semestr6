package extraction;

import lombok.Getter;

import java.util.*;
import java.util.regex.*;
import edu.stanford.nlp.simple.*;

import static extraction.KeyWordsLoader.allKeyWords;

@Getter
public class FeatureExtractor {
    private final Set<String> cities;
    private final Set<String> currencies;
    private final Set<String> names;
    private final Set<String> keywords;

    public FeatureExtractor(Set<String> cities, Set<String> currencies, Set<String> names, Set<String> keywords) {
        this.cities = cities;
        this.currencies = currencies;
        this.names = names;
        this.keywords = keywords;
    }

    public Map<String, Object> extractFeatures(String text) {
        Map<String, Object> features = new HashMap<>();
//        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // 1. Długość tekstu (liczba słów o długości >= 3)
//        String[] words = text.split("\\s+");
        String[] words = tokenize(text);
        int wordCount = (int) Arrays.stream(words).filter(w -> w.length() >= 3).count();
        features.put("length", wordCount);

        // 2. Dominująca waluta
        String dominantCurrency = findDominantEntity(text, currencies);
        features.put("dominant_currency", dominantCurrency);

        // 3. Nazwy miast
        List<String> foundCities = findEntities(text, cities);
        features.put("cities", foundCities);

        // 4. Liczba unikalnych słów
//        String[] wordsWithoutDots = text.replaceAll("[.,!?:;]", "").split("\\s+");
        Set<String> uniqueWords = new HashSet<>(Arrays.stream(words).filter(w -> w.length() >= 3).toList());
        features.put("unique_word_count", uniqueWords.size());

        // 5. Średnia długość słowa
        double avgWordLength = Arrays.stream(words).filter(w -> w.length() >= 3).mapToInt(String::length).average().orElse(0);
        features.put("avg_word_length", avgWordLength);

        // 6. Liczba słów kluczowych w pierwszych 3 zdaniach
        int firstThreeSentences = countKeywordsInFirstSentences(words, 3, keywords);
        features.put("keywords_in_first_3_sentences", firstThreeSentences);

        // 7. Liczba słów zaczynających się wielką literą (nie licząc początku zdania)
        int capitalizedWordCount = countCapitalizedWords(words);
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
                    int cmp = e2.getValue().compareTo(e1.getValue());
                    if (cmp == 0) {
                        return Integer.compare(firstPosition.get(e1.getKey()), firstPosition.get(e2.getKey()));
                    }
                    return cmp;
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("None");
    }

    private int countOccurrences(String text, String word) {
        Matcher matcher = Pattern.compile("(?i)(?<!\\w)" + Pattern.quote(word) + "(?!\\w)", Pattern.CASE_INSENSITIVE).matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
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

    private int countKeywordsInFirstSentences(String[] words, int n, Set<String> keywords) {
        StringBuilder sb = new StringBuilder();
        int senctences = 1;
        for (String word : words) {
            if (senctences == n) {
                break;
            }
            if (Objects.equals(word, ".")) {
                senctences++;
            }
            sb.append(word).append(" ");
        }
        List <String> found = findEntities(sb.toString(), keywords);
        return found.size();
    }

    private int countKeywordOccurrences(String text, Set<String> keywords) {
        int count = 0;
        for (String key : keywords) {
            count += countOccurrences(text, key);
        }
        return count;
    }

    private int countCapitalizedWords(String[] words) {
        int count = 0;
        for (int i = 1; i < words.length; i++) {
            if (Character.isUpperCase(words[i].charAt(0)) && !Objects.equals(words[i - 1], ".") && words[i].length() >= 3) {
                count++;
            }
        }
        return count;
    }

    private String findFirstKeyword(String text, Set<String> keywords) {
        int minIndex = Integer.MAX_VALUE;
        String firstKeyword = "None";

        for (String key : keywords) {
            int index = text.indexOf(key);
            if (index != -1 && index < minIndex) {
                minIndex = index;
                firstKeyword = key;
            }
        }
        return firstKeyword;
    }

    public String[] tokenize(String text) {
        Sentence sentence = new Sentence(text);
        return sentence.words().toArray(new String[0]);
    }
}

