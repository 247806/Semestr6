package classifier;

import extraction.FeatureExtractor;
import extraction.Normalization;
import loading.Article;
import metrics.NGramMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static extraction.KeyWordsLoader.*;
import static extraction.StopListFilter.loadStopList;
import static extraction.StopListFilter.removeStopWords;
import static loading.Load.loadReutersArticles;

public class KNN {
    private List<Article> trainingSet;
    private List<Article> testSet;
    private List<Article> allArticles;
    private final Set<String> stopWords;
    private static final int K = 10;
    private static final double SET_PROPORTION = 0.6;

    public KNN() throws IOException {
        allArticles = loadReutersArticles("src/main/resources/articles");
        this.stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");
        FeatureExtractor featureExtractor = new FeatureExtractor(getCities(), getCurrencies(), getNames(), allKeyWords());
        Normalization normalization = new Normalization();
        NGramMethod ngramMethod = new NGramMethod();
        Map<String, Object> features;
        int counter = 1;
        for (Article article : allArticles) {
            System.out.println(counter++ + " / " + allArticles.size());
            removeStopWords(article.getBody(), stopWords);
            features = featureExtractor.extractFeatures(article.getBody());
            article.setFeatures(features);
        }

    }
}
