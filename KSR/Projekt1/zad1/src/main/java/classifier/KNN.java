package classifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import extraction.FeatureExtractor;
import extraction.Normalization;
import loading.Article;
import metrics.*;
import utils.ArticleData;

import java.io.File;
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
    private static final int K = 2;
    private static final double SET_PROPORTION = 0.6;
    private List<List<Object>> features = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NGramMethod ngramMethod = new NGramMethod();
    private final QualityMeasures qualityMeasures = new QualityMeasures();

    public KNN() throws IOException {
        allArticles = loadReutersArticles("src/main/resources/articles");
        this.stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");
        FeatureExtractor featureExtractor = new FeatureExtractor(getCities(), getCurrencies(), getNames(), allKeyWords());
        Normalization normalization = new Normalization();
        Metrics metrics = new EuclideanMetrics();

//        int counter = 1;
//        for (Article article : allArticles) {
//            System.out.println(counter++ + " / " + allArticles.size());
//            removeStopWords(article.getBody(), stopWords);
//            List<Object> feature = featureExtractor.extractFeatures(article.getBody());
//            article.setFeatures(feature);
//            features.add(feature);
//        }
//
//        normalization.preprocess(features);
//        for (Article article : allArticles) {
//            List<Object> normalizedFeature = normalization.normalize(article.getFeatures());
//            article.setFeatures(normalizedFeature);
//        }
//
//        splitData(allArticles);
//
//
//        saveFeaturesToFile(allArticles, "allArticles.json");
        List <Article> allArticlesLoaded = loadArticlesFromJson("allArticles.json");
        splitData(allArticlesLoaded); // uwazac zeby nie właczyc z splitData z linii 55

        int counter2 = 1;
        for (Article article : testSet) {
            System.out.println(counter2++ + " / " + testSet.size());
            classifyArticle(article, metrics);
        }

        qualityMeasures.calculateAccuracy(testSet);
        qualityMeasures.calculateQualityForPlace(testSet, "usa");
        qualityMeasures.calculateQualityForPlace(testSet, "uk");
        qualityMeasures.calculateQualityForPlace(testSet, "canada");
        qualityMeasures.calculateQualityForPlace(testSet, "france");
        qualityMeasures.calculateQualityForPlace(testSet, "west-germany");
        qualityMeasures.calculateQualityForPlace(testSet, "japan");

        for (Article article : testSet) {;
            if (Objects.equals(article.getPlace(), "west-germany")) {
                System.out.println(article.getPredictedPlace());
            }
        }

    }

    private void classifyArticle(Article article, Metrics metrics) {
        Map<Article, Double> distances = new HashMap<>();
        for (Article trainingArticle : trainingSet) {
            double distance = metrics.calculate(article, trainingArticle, ngramMethod);
            distances.put(trainingArticle, distance);
        }

        List<Map.Entry<Article, Double>> sortedDistances = new ArrayList<>(distances.entrySet());
        sortedDistances.sort(Map.Entry.comparingByValue());

        // Wybieranie K najbliższych artykułów
        List<Article> nearestNeighbors = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            nearestNeighbors.add(sortedDistances.get(i).getKey());
        }

        // Liczenie liczby wystąpień miejsca (place) wśród K najbliższych sąsiadów
        Map<String, Integer> placeCounts = new HashMap<>();
        for (Article neighbor : nearestNeighbors) {
            String place = neighbor.getPlace();
            placeCounts.put(place, placeCounts.getOrDefault(place, 0) + 1);
        }
//        System.out.println("Zliczone miejsca: " + placeCounts);

        // Sortowanie miejsc według liczby wystąpień

        List<Map.Entry<String, Integer>> placeCountsList = new ArrayList<>(placeCounts.entrySet());
        placeCountsList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        if (placeCountsList.size() > 1) {
            if (placeCountsList.get(0).getValue() > placeCountsList.get(1).getValue()) {
                String predictedPlace = placeCountsList.getFirst().getKey();
                article.setPredictedPlace(predictedPlace);
            } else {

                Map<String, Double> placeWeights = new HashMap<>();
                for (int i = 0; i < nearestNeighbors.size(); i++) {
                    Article neighbor = sortedDistances.get(i).getKey();
                    String place = neighbor.getPlace();
                    double weight = 1.0 / sortedDistances.get(i).getValue();  // Odwrotność odległości, im mniejsza odległość, tym większa waga
                    placeWeights.put(place, placeWeights.getOrDefault(place, 0.0) + weight);
                }
                // Wybór miejsca z największą wagą
                String predictedPlace = placeWeights.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get()
                        .getKey();

                // Ustawienie przewidywanego miejsca w artykule
                article.setPredictedPlace(predictedPlace);

            }
        } else {
            String predictedPlace = placeCountsList.getFirst().getKey();
            article.setPredictedPlace(predictedPlace);
        }


    }

    private void splitData(List<Article> allArticles) {
        Map<String, List<Article>> articlesPerCountry = allArticles.stream()
                .collect(Collectors.groupingBy(Article::getPlace));

        for (Map.Entry<String, List<Article>> entry : articlesPerCountry.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().size());
            List<Article> articles = entry.getValue();
//            Collections.shuffle(articles);

            int splitIndex = (int) (articles.size() * SET_PROPORTION);
            this.trainingSet.addAll(articles.subList(0, splitIndex));
            this.testSet.addAll(articles.subList(splitIndex, articles.size()));
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

    public void saveFeaturesToFile(List<Article> articles, String filePath) throws IOException {
        List<ArticleData> data = articles.stream()
                .map(article -> new ArticleData(article.getPlace(), article.getFeatures()))
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filePath), data);
    }

    public static List<Article> loadArticlesFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<ArticleData> data = List.of(mapper.readValue(new File(filePath), ArticleData[].class));

        List<Article> articles = new ArrayList<>();
        for (ArticleData entry : data) {
            Article article = new Article("", entry.getPlace()); // jeśli nie zapisujesz body, zostaw pusty
            article.setFeatures(entry.getFeatures());
            articles.add(article);
        }

        return articles;
    }

}
