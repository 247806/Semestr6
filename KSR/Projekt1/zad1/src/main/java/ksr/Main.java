package ksr;

import ksr.extraction.FeatureExtractor;
import ksr.extraction.Normalization;
import ksr.loading.Article;

import java.io.IOException;
import java.util.*;

import static ksr.extraction.StopListFilter.loadStopList;
import static ksr.loading.Load.loadReutersArticles;

public class Main {
    private static final List<String> TARGET_COUNTRIES = List.of("japan", "usa", "uk", "canada", "west-germany", "france");
    private static final int MIN_ARTICLES = 180;
    private static final int MAX_ARTICLES = 220;
    private static final int K = 10;
    private static final double SET_PROPORTION = 0.6;

    public static void main(String[] args) throws IOException {
        String datasetPath = "src/main/resources/articles";
        List<String> filePaths = new ArrayList<>();
        filePaths.add("src/main/resources/keywords/places.txt");
        filePaths.add("src/main/resources/keywords/currency.txt");
        filePaths.add("src/main/resources/keywords/names.txt");

        Set<String> stopWords = loadStopList("src/main/resources/stop_words/stop_words_english.txt");

//        Stemmer stemmer = new Stemmer();
        FeatureExtractor featureExtractor = new FeatureExtractor(filePaths);
        Normalization normalization = new Normalization();
        List<Map<String, Object>> trainFeatures = new ArrayList<>();
        List<Map<String, Object>> testFeatures = new ArrayList<>();

        try {
            List<Article> loadedTrainArticles = loadReutersArticles(datasetPath, "TRAIN");
            List<Article> trainArticles = selectBalancedTrainSet(loadedTrainArticles);
            System.out.println("Załadowano " + trainArticles.size() + " artykułów trenujących.");

            List<Article> loadedTestArticles = loadReutersArticles(datasetPath, "TEST");
            List<Article> testArticles = calculateTestArticleNumber(trainArticles.size(), loadedTestArticles);
            System.out.println("Załadowano " + testArticles.size() + " artykułów testowych.");

            Map<String, Object> features;
            countCountries(trainArticles);
//            String result = stemmer.stem("I'm testing word Japanese");
//            System.out.println(result);
//            int counter = 1;
//            for (Article article : trainArticles) {
//                System.out.println(counter++ + " / " + trainArticles.size());
//                removeStopWords(article.getBody(), stopWords);
//                features = featureExtractor.extractFeatures(article.getBody());
//                trainFeatures.add(features);
//                article.setFeatures(features);
//            }
//            counter = 1;
//            for (Article article : testArticles) {
//                System.out.println(counter++ + " / " + testArticles.size());
//                removeStopWords(article.getBody(), stopWords);
//                features = featureExtractor.extractFeatures(article.getBody());
//                testFeatures.add(features);
//                article.setFeatures(features);
//            }
//
//            normalization.preprocess(trainFeatures);
//            for (int i = 0; i < 10; i++) {
//                System.out.println(trainFeatures.get(i));
//                System.out.println(normalization.normalize(trainFeatures.get(i)));
//            }
//
//            trainFeatures.stream().limit(10).forEach(System.out::println);
//            testFeatures.stream().limit(10).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void countCountries(List<Article> articles) {
        int countJ = 0;
        int countUSA = 0;
        int countF = 0;
        int countC = 0;
        int countG = 0;
        int countUK = 0;
        for (Article a : articles) {
            if (a.getPlace().contains("japan")) {
                countJ++;
            } else if (a.getPlace().contains("usa")) {
                countUSA++;
            } else if (a.getPlace().contains("france")) {
                countF++;
            } else if (a.getPlace().contains("canada")) {
                countC++;
            } else if (a.getPlace().contains("uk")) {
                countUK++;
            } else if (a.getPlace().contains("west-germany")) {
                countG++;
            }
        }
        System.out.println("Japan: " + countJ + " USA: " + countUSA + " Canada: " + countC + " UK: "
                + countUK + " Germany: " + countG + " France: " + countF);
    }

    private static List<Article> selectBalancedTrainSet(List<Article> trainArticles) {
        Map<String, List<Article>> countryArticles = new HashMap<>();

        for (Article article : trainArticles) {
            for (String country : TARGET_COUNTRIES) {
                if (article.getPlace().contains(country)) {
                    countryArticles.putIfAbsent(country, new ArrayList<>());
                    countryArticles.get(country).add(article);
                    break;
                }
            }
        }

        List<Article> selectedTrainArticles = new ArrayList<>();
        Random random = new Random();

        for (String country : TARGET_COUNTRIES) {
            List<Article> articles = countryArticles.getOrDefault(country, new ArrayList<>());

            if (articles.size() < MIN_ARTICLES) {
                int size = articles.size();
                int targetCount = MIN_ARTICLES + random.nextInt(size - MIN_ARTICLES + 1);
                Collections.shuffle(articles);
                selectedTrainArticles.addAll(articles.subList(0, Math.min(targetCount, articles.size())));
            } else {
                int targetCount = MIN_ARTICLES + random.nextInt(MAX_ARTICLES - MIN_ARTICLES + 1);
                Collections.shuffle(articles);
                selectedTrainArticles.addAll(articles.subList(0, Math.min(targetCount, articles.size())));
            }
        }

        System.out.println("Wybrano " + selectedTrainArticles.size() + " artykułów do zbioru treningowego.");
        return selectedTrainArticles;
    }

    private static List<Article> calculateTestArticleNumber (int y, List<Article> testArticles) {
        int temp = (int) (y * (1 - Main.SET_PROPORTION) / Main.SET_PROPORTION);
        Collections.shuffle(testArticles);
        return new ArrayList<>(testArticles.subList(0, temp));
    }

}