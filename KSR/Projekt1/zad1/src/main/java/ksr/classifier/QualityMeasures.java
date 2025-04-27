package ksr.classifier;

import ksr.loading.Article;

import java.util.List;

public class QualityMeasures {

    public double calculateAccuracy(List<Article> testSet) {
        int correctPredictions = 0;
        for (Article testArticle : testSet) {
            if (testArticle.getPredictedPlace().equals(testArticle.getPlace())) {
                correctPredictions++;
            }
        }
//        System.out.println((double) correctPredictions / testSet.size());
        return (double) correctPredictions / testSet.size();

    }

    public Double[] calculateAverageQuality(Double [][] qualityMeasure) {
        double precisionSum = 0;
        double recallSum = 0;
        double f1ScoreSum = 0;

        for (int i = 0; i < 6; i++) {
            precisionSum += qualityMeasure[i][0];
            recallSum += qualityMeasure[i][1];
            f1ScoreSum += qualityMeasure[i][2];
        }

        double averagePrecision = precisionSum / 6;
        double averageRecall = recallSum / 6;
        double averageF1Score = f1ScoreSum / 6;

        return new Double[]{averagePrecision, averageRecall, averageF1Score};
    }

    public Double[] calculateQualityForPlace(List<Article> testSet, String place){
        double precision = calculatePrecision(testSet, place);
        double recall = calculateRecall(testSet, place);
        double f1Score = calculateF1Score(precision, recall);

//        System.out.println("Quality measures for place: "+ place);
//        System.out.println("Precision: " + precision);
//        System.out.println("Recall: " + recall);
//        System.out.println("F1 Score: " + f1Score);
        return new Double[]{precision, recall, f1Score};
    }


    private double calculatePrecision(List<Article> testSet, String place) {
        int truePositives = 0;
        int falsePositives = 0;
        for (Article testArticle : testSet) {
            if (testArticle.getPredictedPlace().equals(place)) {
                if (testArticle.getPredictedPlace().equals(testArticle.getPlace())) {
                    truePositives++;
                } else {
                    falsePositives++;
                }
            }
        }
        return (truePositives + falsePositives) == 0 ? 0.0 : (double) truePositives / (truePositives + falsePositives);
    }

    private double calculateRecall(List<Article> testSet, String place) {
        int truePositives = 0;
        int falseNegatives = 0;
        for (Article testArticle : testSet) {
            if (testArticle.getPlace().equals(place)) {
                if (testArticle.getPredictedPlace().equals(testArticle.getPlace())) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            }
        }
        return (double) truePositives / (truePositives + falseNegatives);
    }

    public double calculateF1Score(double precision, double recall) {
        if (precision + recall == 0) {
            return 0;
        }
        return 2 * (precision * recall) / (precision + recall);
    }

}
