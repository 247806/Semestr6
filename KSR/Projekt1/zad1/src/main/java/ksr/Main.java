package ksr;

import ksr.classifier.KNN;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int k = 10;
        double proportion = 0.6;
        String metric = "Euklidesowa";
        KNN knn = new KNN(k, proportion, metric);

    }
}