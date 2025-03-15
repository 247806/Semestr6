import extraction.FeatureExtractor;
import loading.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static loading.Load.loadReutersArticles;

public class Main {
    public static void main(String[] args) {
        String datasetPath = "src/main/resources/articles";
        List<String> filePaths = new ArrayList<>();
        filePaths.add("src/main/resources/keywords/places.txt");
        filePaths.add("src/main/resources/keywords/currency.txt");
        filePaths.add("src/main/resources/keywords/names.txt");
        try {
            FeatureExtractor featureExtractor = new FeatureExtractor(filePaths);
            List<Article> articles = loadReutersArticles(datasetPath);
            System.out.println("Załadowano " + articles.size() + " artykułów.");
            articles.stream().limit(1).forEach(System.out::println);
            Map<String, Object> features = new HashMap<>();
            features = featureExtractor.extractFeatures("Pierwsze zdanie New York. Drugie zdanie dlrs.");
            System.out.println(features);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
