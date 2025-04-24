package ksr;

import ksr.classifier.KNN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        int k = 10;
        double proportion = 0.6;
        String metric = "Euklidesowa";
        List<Integer> numbers = new ArrayList<>(List.of(1));
        KNN knn = new KNN(k, proportion, metric, numbers);
        long end = System.nanoTime();
        long duration = end - start;

        System.out.println("Czas wykonania: " + duration / 1_000_000 + " ms");
    }
}