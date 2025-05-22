package ksr.zad2.fuzzy.lingustic;

import lombok.Data;

import java.util.List;

@Data
public class Summary {
    Quantifier quantifier;
    List<Summarizer> summarizer;

    public Summary(Quantifier quantifier, List<Summarizer> summarizer) {
        this.quantifier = quantifier;
        this.summarizer = summarizer;
    }

    public String singleSummarization() {
        double truth = degreeOfTruth();
        StringBuilder sb = new StringBuilder();
        sb.append(quantifier.getName()).append(" measurements ma ").append(summarizer.getFirst().getName()).append(" temperaturę ").append(" [")
                .append(truth).append("] ");
        return sb.toString();
    }

    public String doubleSummarization() {
        double truth = degreeOfTruth();
        StringBuilder sb = new StringBuilder();
        sb.append(quantifier.getName()).append(" measurements ma ").append(summarizer.getFirst().getName()).append(" temperaturę ")
                .append("oraz ").append(summarizer.get(1).getName()).append(" wilgotność ").append(" [")
                .append(truth).append("] ");
        return sb.toString();
    }

    public double degreeOfTruth() {
        if (summarizer.size() == 1) {
            if (quantifier.getType().equals("ABSOLUTE")) {
                //TODO SPRAWDZIĆ CZY TO DOBRZE
                return quantifier.getFuzzySet().membership(summarizer.getFirst().getFuzzySet()
                        .cardinality(summarizer.getFirst().getData()));
            } else {
                return quantifier.getFuzzySet().membership(summarizer.getFirst().getFuzzySet()
                        .fuzzyCardinality(summarizer.getFirst().getData()) / summarizer.getFirst().getData().size());
            }
        } else {
            List<Double> data1 = summarizer.get(0).getData();
            List<Double> data2 = summarizer.get(1).getData();

            double sum = 0.0;
            for (int i = 0; i < data1.size(); i++) {
                double mu1 = summarizer.get(0).getFuzzySet().membership(data1.get(i));
                double mu2 = summarizer.get(1).getFuzzySet().membership(data2.get(i));
                sum += Math.min(mu1, mu2);
            }

            double average = sum / data1.size();

            if (quantifier.getType().equals("ABSOLUTE")) {
                return quantifier.getFuzzySet().membership(sum);
            } else {
                return quantifier.getFuzzySet().membership(average);
            }
        }
    }
}
