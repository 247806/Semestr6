package ksr.zad2.fuzzy;

import ksr.zad2.model.Measurements;
import lombok.Data;

import java.util.List;

@Data
public class DoubleSubjectSummary {
    private final Quantifier quantifier;
    private final String subject1;
    private final String subject2;
    private final LinguisticTerm subject1Summarizer;
    private final LinguisticTerm subject2Summarizer;
    private LinguisticTerm qualifier;

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
        summary.append(quantifier.getName()).append(" ").append(subject1).append(" w porównaniu do ")
                .append(subject2).append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

        System.out.println(summary.toString());

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
        summary.append(quantifier.getName()).append(" ").append(subject1).append(" w porównaniu do ")
                .append(subject2).append(", które są/mają ").append(qualifier.getName()).append(" jest/ma ").append(subject1Summarizer.getName())
                .append(" [").append(result).append("]. ");

        System.out.println(summary.toString());

    }



}
