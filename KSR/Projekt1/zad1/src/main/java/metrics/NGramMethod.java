package metrics;

public class NGramMethod {
    public double calculateNGramSimilarity(String text1, String text2, int n1, int n2) {

        String word1 = text1.replaceAll("\\s+", "");
        String word2 = text2.replaceAll("\\s+", "");

        int N = Math.max(word1.length(), word2.length());
        int shorterLength = Math.min(word1.length(), word2.length());
        int commonNGrams = 0;

        for (int i = n1; i <= n2; i++) {
            for (int j = 0; j <= shorterLength - i; j++) {
                String nGram1 = word1.substring(j, j + i);
                System.out.println("nGram1: " + nGram1);
                if (word2.toLowerCase().contains(nGram1.toLowerCase())) {
                    commonNGrams++;
                }
            }
        }

        double f = (double) 2 / ((N - n1 + 1) * (N - n1 + 2) - (N - n2 + 1) * (N - n2));
        return f * commonNGrams;
    }
}
