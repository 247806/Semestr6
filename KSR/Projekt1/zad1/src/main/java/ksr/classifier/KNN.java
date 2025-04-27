package ksr.classifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ksr.extraction.FeatureExtractor;
import ksr.extraction.Normalization;
import ksr.loading.Article;
import ksr.metrics.*;
import ksr.utils.ArticleData;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ksr.extraction.KeyWordsLoader.*;
import static ksr.extraction.StopListFilter.loadStopList;
import static ksr.extraction.StopListFilter.removeStopWords;
import static ksr.loading.Load.loadReutersArticles;

public class KNN {
    private List<Article> trainingSet = new ArrayList<>();
    private List<Article> testSet = new ArrayList<>();
    private final int K;
    private final double SET_PROPORTION;
    private final NGramMethod ngramMethod = new NGramMethod();

    @Getter
    public double accuracy;

    @Getter
    public Double [][] qualityMeasure = new Double[6][3];

    public KNN(int k,double proportion, String metric, List<Integer> numbers) throws IOException {
//        long start = System.nanoTime();
        K = k;
        SET_PROPORTION = proportion;

        List<Article> allArticles = loadReutersArticles("src/main/resources/articles");
        Set<String> stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");
        FeatureExtractor featureExtractor = new FeatureExtractor(getCities(), getCurrencies(), getNames(), allKeyWords());
        Normalization normalization = new Normalization();
        Metrics metrics = switch (metric) {
            case "Euklidesowa" -> new EuclideanMetrics();
            case "Uliczna" -> new ManhattanMetrics();
            case "Czebyszewa" -> new CzebyszewMetrics();
            default -> throw new IllegalArgumentException("Unknown metric: " + metric);
        };


        if (!new File("allArticles.json").exists()) {
//            int counter = 1;
            List<List<Object>> features = new ArrayList<>();
            for (Article article : allArticles) {
//                System.out.println(counter++ + " / " + allArticles.size());
                removeStopWords(article.getBody(), stopWords);
                List<Object> feature = featureExtractor.extractFeatures(article.getBody());
                article.setFeatures(feature);
                features.add(feature);
            }

            normalization.preprocess(features);
            for (Article article : allArticles) {
                List<Object> normalizedFeature = normalization.normalize(article.getFeatures());
                article.setFeatures(normalizedFeature);
            }
            Collections.shuffle(allArticles);
            splitData(allArticles);
            saveFeaturesToFile(allArticles, "allArticles.json");
            if (!numbers.isEmpty()) {
                this.testSet = deleteFeature(testSet, numbers);
                this.trainingSet = deleteFeature(trainingSet, numbers);
            }

        } else {
            List <Article> allArticlesLoaded = loadArticlesFromJson("allArticles.json");
            splitData(allArticlesLoaded);
            if (!numbers.isEmpty()) {
                this.testSet = deleteFeature(testSet, numbers);
                this.trainingSet = deleteFeature(trainingSet, numbers);
            }
        }

        testSet.parallelStream().forEach(article -> classifyArticle(article, metrics));


        QualityMeasures qualityMeasures = new QualityMeasures();
        accuracy = qualityMeasures.calculateAccuracy(testSet);
//        System.out.println("Accuracy: " + accuracy);
        qualityMeasure[0] = qualityMeasures.calculateQualityForPlace(testSet, "usa");
        qualityMeasure[1] = qualityMeasures.calculateQualityForPlace(testSet, "uk");
        qualityMeasure[2] = qualityMeasures.calculateQualityForPlace(testSet, "canada");
        qualityMeasure[3] = qualityMeasures.calculateQualityForPlace(testSet, "france");
        qualityMeasure[4] = qualityMeasures.calculateQualityForPlace(testSet, "west-germany");
        qualityMeasure[5] = qualityMeasures.calculateQualityForPlace(testSet, "japan");

//        System.out.println("All quality measure: " + Arrays.deepToString(qualityMeasure));

//        long end = System.nanoTime();
//        long duration = end - start;
//
//        System.out.println("Czas wykonania: " + duration / 1_000_000 + " ms");

    }

    private void classifyArticle(Article article, Metrics metrics) {
        // Obliczanie odległości do wszystkich artykułów w zbiorze treningowym
        List<Map.Entry<Article, Double>> distances = trainingSet.stream()
                .map(trainingArticle -> Map.entry(trainingArticle, metrics.calculate(article, trainingArticle, ngramMethod)))
                .sorted(Map.Entry.comparingByValue()).toList();

        // Wybieranie K najbliższych sąsiadów
        List<Article> nearestNeighbors = distances.stream()
                .limit(K)
                .map(Map.Entry::getKey)
                .toList();

        // Liczenie liczby wystąpień kraju wśród K najbliższych sąsiadów
        Map<String, Long> placeCounts = nearestNeighbors.stream()
                .collect(Collectors.groupingBy(Article::getPlace, Collectors.counting()));

        long maxValue = placeCounts.values().stream().max(Long::compareTo).orElse(0L);

        // Wybieranie krajów o największej liczbie wystąpień
        List<String> placeCountsList = placeCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxValue)
                .map(Map.Entry::getKey)
                .toList();

        if (placeCountsList.size() > 1) {
            String predictedPlace = nearestNeighbors.stream().collect(Collectors.groupingBy(
                    Article::getPlace,
                    Collectors.summingDouble(article1 -> 1.0 / distances.stream()
                            .filter(entry -> entry.getKey().equals(article1))
                            .findFirst()
                            .map(Map.Entry::getValue)
                            .orElse(1.0))) // Odwrotność odległości
                    ).entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
            article.setPredictedPlace(predictedPlace);

        } else {
            String predictedPlace = placeCountsList.getFirst();
            article.setPredictedPlace(predictedPlace);
        }


    }

    private void splitData(List<Article> allArticles) {
        allArticles.stream().collect(Collectors.groupingBy(Article::getPlace)).forEach(
                (place, articles) -> {
                    int splitIndex = (int) (articles.size() * SET_PROPORTION);
                    this.trainingSet.addAll(articles.subList(0, splitIndex));
                    this.testSet.addAll(articles.subList(splitIndex, articles.size()));
                }
        );

//        System.out.println("Zbiór treningowy: " + trainingSet.size());
//        System.out.println("Zbiór testowy: " + testSet.size());
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
            Article article = new Article("", entry.getPlace());
            article.setFeatures(entry.getFeatures());
            articles.add(article);
        }

        return articles;
    }

    private List<Article> deleteFeature(List<Article> articles, List<Integer> numbers) {
        List<Article> newList = new ArrayList<>();
        for (Article a : articles) {
            int counter = 0;
            List<Object> features = a.getFeatures();
            for (Integer i : numbers) {
                features.remove(i - counter);
                counter++;
            }
            a.setFeatures(features);
            newList.add(a);
        }

        return newList;
    }

}
