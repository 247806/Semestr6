package ksr.metrics;

import java.util.HashSet;
import java.util.Set;

public class NGramMethod {
    public double calculateNGramSimilarity(String text1, String text2, int n1, int n2) {

        String word1 = text1.replaceAll("\\s+", "").toLowerCase();
        String word2 = text2.replaceAll("\\s+", "").toLowerCase();

        int N = Math.max(word1.length(), word2.length());
        int shorterLength = Math.min(word1.length(), word2.length());
        int commonNGrams = 0;

        for (int i = n1; i <= n2; i++) {
            Set<String> nGrams1 = new HashSet<>();
            Set<String> nGrams2 = new HashSet<>();

            for (int j = 0; j <= shorterLength - i; j++) {
                String nGram1 = word1.substring(j, j + i);
                nGrams1.add(nGram1.toLowerCase());
            }

            for (int j = 0; j <= shorterLength - i; j++) {
                String nGram2 = word2.substring(j, j + i);
                nGrams2.add(nGram2.toLowerCase());
            }

            for (String nGram1 : nGrams1) {
                if (nGrams2.contains(nGram1)) {
                    commonNGrams++;
                }
            }
        }

        double f = (double) 2 / ((N - n1 + 1) * (N - n1 + 2) - (N - n2 + 1) * (N - n2));
        return f * commonNGrams;
    }
}
