package ksr.zad2.fuzzy;

import lombok.Data;


import java.util.List;

@Data
public class Summary {
    private Quantifier quantifier;
    private List<Summarizer> summarizer;
    private Qualifier qualifier;

    public Summary(Quantifier quantifier, List<Summarizer> summarizer) {
        this.quantifier = quantifier;
        this.summarizer = summarizer;
    }

    public Summary(Quantifier quantifier, List<Summarizer> summarizer, Qualifier qualifier) {
        this.quantifier = quantifier;
        this.summarizer = summarizer;
        this.qualifier = qualifier;
    }

    public String summarization() {
        double truth = degreeOfTruth();
        StringBuilder summaryText = new StringBuilder(quantifier.getName() + " measurements ma ");

        for (int i = 0; i < summarizer.size(); i++) {
            if (i > 0) {
                summaryText.append(" oraz ");
            }
            summaryText.append(summarizer.get(i).getName());
        }

        summaryText.append(" [" + truth + "] ");
        return summaryText.toString();
    }


    public String qualifiedSummarization() {
        double truth = degreeOfTruthWithQualifier();
        return quantifier.getName() + " measurements które są " + qualifier.getName() + " ma " +
                summarizer.getFirst().getName() + " temperaturę " + " [" +
                truth + "] ";
    }

    public double degreeOfTruth() {
        if (summarizer.size() == 1) {
            if (!quantifier.isRelative()) {
                return quantifier.getFuzzySet().membership(summarizer.getFirst().getFuzzySet()
                        .cardinality(summarizer.getFirst().getData()));
            } else {
                return quantifier.getFuzzySet().membership(summarizer.getFirst().getFuzzySet()
                        .fuzzyCardinality(summarizer.getFirst().getData()) / summarizer.getFirst().getData().size());
            }
        } else {
            if (quantifier.isRelative()) {
                double sum = 0.0;
                for (int i = 0; i < summarizer.getFirst().getData().size(); i++) {
                    double minValue = Double.MAX_VALUE;
                    for (Summarizer summarizer : summarizer) {
                        minValue = Math.min(minValue, summarizer.getFuzzySet().membership(summarizer.getData().get(i)));
                    }
                    sum += minValue;
                }
                double average = sum / summarizer.getFirst().getData().size();
                return quantifier.getFuzzySet().membership(average);
            } else {
                double sum = 0.0;
                double size = summarizer.size();
                for (int i = 0; i < summarizer.getFirst().getData().size(); i++) {
                    double temp = 0.0;
                    for (Summarizer summarizer : summarizer) {
                        temp += summarizer.getFuzzySet().membership(summarizer.getData().get(i));
                    }
                    if (temp == size) {
                        sum += 1.0;
                    }
                }
                return sum;
            }
        }
    }


    //TODO ZROBIC Z WIELOMA SUMARYZATORAMI JESZCZE
    public double degreeOfTruthWithQualifier() {
        List<Double> dataQ = qualifier.getData();
        List<Double> dataS = summarizer.getFirst().getData();

        double sum = 0.0;

        for (int i = 0; i < dataQ.size(); i++) {
            double muQ = qualifier.getFuzzySet().membership(dataQ.get(i));
            double muS = summarizer.getFirst().getFuzzySet().membership(dataS.get(i));
            sum += Math.min(muQ, muS);
        }

        if (sum == 0.0) return 0.0;

        return quantifier.getFuzzySet().membership(sum / qualifier.getFuzzySet().cardinality(dataS));
    }

    public double degreeOfImprecision() {
        double sum = 0.0;
        for (Summarizer value : summarizer) {
            sum *= value.getFuzzySet().degreeOfFuzziness(value.getData());
        }
        return 1 - (Math.pow(sum, 1.0 / summarizer.size()));
    }

    public double degreeOfCovering() {
        double t = 0.0;
        double h = 0.0;
        for (int i = 0; i < summarizer.getFirst().getData().size(); i++) {
            if (summarizer.getFirst().getFuzzySet().contains(summarizer.getFirst().getData().get(i)) > 0.0 &&
                    qualifier.getFuzzySet().contains(qualifier.getData().get(i)) > 0) {
                t += 1;
            }
            if (qualifier.getFuzzySet().contains(qualifier.getData().get(i)) > 0.0) {
                h += 1;
            }
        }
        return t / h;
    }

}
