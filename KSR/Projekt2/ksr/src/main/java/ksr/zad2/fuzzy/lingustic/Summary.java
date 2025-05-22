package ksr.zad2.fuzzy.lingustic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class Summary {
    private String text;
    private double truth;
    List<Double> data;
    LinguisticVariable variable;
    Quantifier quantifier;
    Summarizer summarizer;

    public Summary(List<Double> data, LinguisticVariable variable, Quantifier quantifier, Summarizer summarizer) {
        this.data = data;
        this.variable = variable;
        this.quantifier = quantifier;
        this.summarizer = summarizer;
    }

    public String makeSummarization() {
        double temp = summarizer.getFuzzySet().fuzzyCardinality(data) / data.size();
        double truth = quantifier.getFuzzySet().membership(temp);
        StringBuilder sb = new StringBuilder();
        sb.append(quantifier.getName()).append(" measurements ma ").append(summarizer.getName()).append(" temperaturę ").append(" [")
                .append(truth).append("] ");
        return sb.toString();
    }

}
