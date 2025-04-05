package classifier;

import extraction.FeatureExtractor;
import extraction.Normalization;
import loading.Article;
import metrics.NGramMethod;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static extraction.KeyWordsLoader.*;
import static extraction.StopListFilter.loadStopList;
import static extraction.StopListFilter.removeStopWords;
import static loading.Load.loadReutersArticles;

public class KNN {
    private final List<Article> trainingSet = new ArrayList<>();
    private final List<Article> testSet = new ArrayList<>();
    private final List<Article> allArticles;
    private final Set<String> stopWords;
    private static final int K = 10;
    private static final double SET_PROPORTION = 0.6;
    private List<List<Object>> features = new ArrayList<>();

    public KNN() throws IOException {
        allArticles = loadReutersArticles("src/main/resources/articles");
        this.stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");
        FeatureExtractor featureExtractor = new FeatureExtractor(getCities(), getCurrencies(), getNames(), allKeyWords());
        Normalization normalization = new Normalization();
        NGramMethod ngramMethod = new NGramMethod();


        int counter = 1;
        for (Article article : allArticles) {
            System.out.println(counter++ + " / " + allArticles.size());
            removeStopWords(article.getBody(), stopWords);
            List<Object> feature = featureExtractor.extractFeatures(article.getBody());
            article.setFeatures(feature);
            features.add(feature);
        }

        normalization.preprocess(features);
        for (int i = 0; i < 5; i++) {
            System.out.println(features.get(i));
            System.out.println(normalization.normalize(features.get(i)));

        }

        this.splitData();

    }

    private void splitData() {
        Map<String, List<Article>> articlesPerCountry = this.allArticles.stream()
                .collect(Collectors.groupingBy(Article::getPlace));

        System.out.println("Wszytkie artykuly: " + allArticles.size());

        for (Map.Entry<String, List<Article>> entry : articlesPerCountry.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().size());
            List<Article> articles = entry.getValue();
            Collections.shuffle(articles);

            int splitIndex = (int) (articles.size() * SET_PROPORTION);
            trainingSet.addAll(articles.subList(0, splitIndex));
            testSet.addAll(articles.subList(splitIndex, articles.size()));
        }

        System.out.println("Zbiór treningowy: " + trainingSet.size());
        System.out.println("Zbiór testowy: " + testSet.size());
        Map<String, List<Article>> articlesTraining = this.trainingSet.stream()
                .collect(Collectors.groupingBy(Article::getPlace));
        Map<String, List<Article>> articlesTest = this.testSet.stream()
                .collect(Collectors.groupingBy(Article::getPlace));

        for (Map.Entry<String, List<Article>> entry : articlesTraining.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().size());
        }

        for (Map.Entry<String, List<Article>> entry : articlesTest.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().size());
        }

    }
}
