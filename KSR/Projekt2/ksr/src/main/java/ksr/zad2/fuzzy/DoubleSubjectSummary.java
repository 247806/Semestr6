package ksr.zad2.fuzzy;

import ksr.zad2.model.Measurements;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DoubleSubjectSummary {
    private Quantifier quantifier;
    private final String subject1;
    private final String subject2;
    private final LinguisticTerm subject1Summarizer;
    private final LinguisticTerm subject2Summarizer;
    private LinguisticTerm qualifier;
    private double t1;
    private double t2;
    private double t3;
    private double t4;
    private String summary;

    public DoubleSubjectSummary(Quantifier quantifier, String subject1, String subject2, LinguisticTerm subject1Summarizer, LinguisticTerm subject2Summarizer) {
        this.quantifier = quantifier;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject1Summarizer = subject1Summarizer;
        this.subject2Summarizer = subject2Summarizer;

    }

    public DoubleSubjectSummary(Quantifier quantifier, String subject1, String subject2, LinguisticTerm subject1Summarizer, LinguisticTerm subject2Summarizer, LinguisticTerm qualifier) {
        this.quantifier = quantifier;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject1Summarizer = subject1Summarizer;
        this.subject2Summarizer = subject2Summarizer;
        this.qualifier = qualifier;
    }

    public DoubleSubjectSummary(String subject1, String subject2, LinguisticTerm subject1Summarizer, LinguisticTerm subject2Summarizer) {
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject1Summarizer = subject1Summarizer;
        this.subject2Summarizer = subject2Summarizer;
    }

    public void firstForm() {

        double numerator = subject1Summarizer.getFuzzySet().fuzzyCardinality(subject1Summarizer.getData()) /
                subject1Summarizer.getData().size();

        double denominator = numerator + subject2Summarizer.getFuzzySet().fuzzyCardinality(subject2Summarizer.getData()) /
                subject2Summarizer.getData().size();

        double result;

        if (denominator == 0) {
            result = 0.0;
        } else {
            result = quantifier.getFuzzySet().membership(numerator / denominator);
        }
        StringBuilder summary = new StringBuilder();
        summary.append(quantifier.getName()).append(" pomiarów z ").append(subject1).append(" w porównaniu do pomiarów z ")
                .append(subject2).append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

        this.t1 = result;
        if (result > 0.01 & result < 0.20) {
            System.out.println(summary.toString());
        }
        this.summary = summary.toString();
    }

    public void secondForm() {
        double numerator = subject1Summarizer.getFuzzySet().fuzzyCardinality(subject1Summarizer.getData()) /
                subject1Summarizer.getData().size();

        double sum = 0.0;

        for (int i = 0; i < qualifier.getData().size(); i++) {
            double minValue = Double.MAX_VALUE;
            minValue = Math.min(minValue, subject2Summarizer.getFuzzySet().membership(subject2Summarizer.getData().get(i)));
            sum += minValue;
        }

        double denominator = numerator + (sum / subject2Summarizer.getData().size());

        double result;

        if (denominator == 0) {
            result = 0.0;
        } else {
            result = quantifier.getFuzzySet().membership(numerator / denominator);
        }
        StringBuilder summary = new StringBuilder();
        summary.append(quantifier.getName()).append(" pomiarów z ").append(subject1).append(" w porównaniu do pomiarów z ")
                .append(subject2).append(", które są/mają ").append(qualifier.getName()).append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

        if (result > 0.01 & result < 0.20) {
            System.out.println(summary.toString());
        }
        this.summary = summary.toString();
    }

    public void thirdForm() {
        double sum = 0.0;
        for (int i = 0; i < subject1Summarizer.getData().size(); i++) {
            double minValue = Double.MAX_VALUE;
            minValue = Math.min(minValue, subject1Summarizer.getFuzzySet().membership(subject1Summarizer.getData().get(i)));
            sum += minValue;
        }

        double numerator = sum / subject1Summarizer.getData().size();

        double denominator = numerator / subject1Summarizer.getData().size() +
                subject2Summarizer.getFuzzySet().fuzzyCardinality(subject2Summarizer.getData()) /
                subject2Summarizer.getData().size();

        double result;

        if (denominator == 0) {
            result = 0.0;
        } else {
            result = quantifier.getFuzzySet().membership(numerator / denominator);
        }

        StringBuilder summary = new StringBuilder();
        summary.append(quantifier.getName()).append(" pomiarów z ").append(subject1).append(", które są/mają ").append(qualifier.getName())
                .append(", w porównaniu do pomiarów z ").append(subject2).append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

        if (result > 0.01 & result < 0.20) {
            System.out.println(summary.toString());
        }
        this.summary = summary.toString();
    }

    public void fourthForm() {
        double m = (double) subject1Summarizer.getData().size() / (subject1Summarizer.getData().size() + subject2Summarizer.getData().size());
        double b = subject1Summarizer.getData().stream().mapToDouble(v -> subject1Summarizer.getFuzzySet().membership(v)).average().orElse(0.0);
        double a = subject2Summarizer.getData().stream().mapToDouble(v -> subject2Summarizer.getFuzzySet().membership(v)).average().orElse(0.0);

        System.out.println("Subject1: " + subject1 + " - " + subject1Summarizer.getName());
        System.out.println("Subject2: " + subject2 + " - " + subject2Summarizer.getName());
        System.out.println("Subject1 size: " + subject1Summarizer.getData().size());
        System.out.println("Subject2 size: " + subject2Summarizer.getData().size());

        double inc = Math.min(1, 1 - a + b);

        double result = 1 - m * inc;

        StringBuilder summary = new StringBuilder();
        summary.append("Więcej pomiarów z ").append(subject1).append(" niż pomiarów z ").append(subject2)
                .append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

//        if (result > 0.01) {
            System.out.println(summary.toString());
//        }
        this.summary = summary.toString();
    }

}
