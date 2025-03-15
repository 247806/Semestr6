import extraction.FeatureExtractor;
import extraction.Stemmer;
import loading.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static extraction.StopListFilter.loadStopList;
import static extraction.StopListFilter.removeStopWords;
import static loading.Load.loadReutersArticles;

public class Main {
    public static void main(String[] args) throws IOException {
        String datasetPath = "src/main/resources/articles";
        List<String> filePaths = new ArrayList<>();
        filePaths.add("src/main/resources/keywords/places.txt");
        filePaths.add("src/main/resources/keywords/currency.txt");
        filePaths.add("src/main/resources/keywords/names.txt");

        Set<String> stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");

//        Stemmer stemmer = new Stemmer();
        FeatureExtractor featureExtractor = new FeatureExtractor(filePaths);
        List<Map<String, Object>> trainFeatures = new ArrayList<>();
        List<Map<String, Object>> testFeatures = new ArrayList<>();

        try {
            List<Article> trainArticles = loadReutersArticles(datasetPath, "TRAIN");
            System.out.println("Załadowano " + trainArticles.size() + " artykułów trenujących.");
            List<Article> testArticles = loadReutersArticles(datasetPath, "TEST");
            System.out.println("Załadowano " + testArticles.size() + " artykułów testowych.");
//            String result = stemmer.stem("I'm testing word Japanese");
//            System.out.println(result);
            int counter = 0;
            for (Article article : trainArticles) {
                System.out.println(counter++ + " / " + trainArticles.size());
                removeStopWords(article.getBody(), stopWords);
                trainFeatures.add(featureExtractor.extractFeatures(article.getBody()));
            }

            for (Article article : testArticles) {
                System.out.println(counter++ + " / " + testArticles.size());
                removeStopWords(article.getBody(), stopWords);
                testFeatures.add(featureExtractor.extractFeatures(article.getBody()));
            }
//            for (int i = 0; i < 3; i++) {
//                System.out.println(testArticles.get(i).getBody());
//                String article = removeStopWords("U.S. Dollar, dmk, yen", stopWords);
//                System.out.println(article);
//                features.add(featureExtractor.extractFeatures(article));
//                System.out.println(features.get(i));
//            }

            trainFeatures.stream().limit(1).forEach(System.out::println);
            testArticles.stream().limit(1).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
