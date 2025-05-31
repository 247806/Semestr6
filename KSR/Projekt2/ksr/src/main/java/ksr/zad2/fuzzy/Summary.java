package ksr.zad2.fuzzy;

import lombok.Data;


import java.util.List;

@Data
public class Summary {
    private Quantifier quantifier;
    private List<Summarizer> summarizers;
    private Qualifier qualifier;

    public Summary(Quantifier quantifier, List<Summarizer> summarizers) {
        this.quantifier = quantifier;
        this.summarizers = summarizers;
    }

    public Summary(Quantifier quantifier, List<Summarizer> summarizers, Qualifier qualifier) {
        this.quantifier = quantifier;
        this.summarizers = summarizers;
        this.qualifier = qualifier;
    }

    public String summarization() {
        double truth = degreeOfTruth();
        StringBuilder summaryText = new StringBuilder(quantifier.getName() + " measurements ma ");

        for (int i = 0; i < summarizers.size(); i++) {
            if (i > 0) {
                summaryText.append(" oraz ");
            }
            summaryText.append(summarizers.get(i).getName());
        }

        summaryText.append(" [" + truth + "] ");
        return summaryText.toString();
    }


    public String qualifiedSummarization() {
        double truth = degreeOfTruthWithQualifier();
        return quantifier.getName() + " measurements które są " + qualifier.getName() + " ma " +
                summarizers.getFirst().getName() + " temperaturę " + " [" +
                truth + "] ";
    }

    public double degreeOfTruth() {
        if (summarizers.size() == 1) {
            if (!quantifier.isRelative()) {
                return quantifier.getFuzzySet().membership(summarizers.getFirst().getFuzzySet()
                        .cardinality(summarizers.getFirst().getData()));
            } else {
                return quantifier.getFuzzySet().membership(summarizers.getFirst().getFuzzySet()
                        .fuzzyCardinality(summarizers.getFirst().getData()) / summarizers.getFirst().getData().size());
            }
        } else {
            if (quantifier.isRelative()) {
                double sum = 0.0;
                for (int i = 0; i < summarizers.getFirst().getData().size(); i++) {
                    double minValue = Double.MAX_VALUE;
                    for (Summarizer summarizer : summarizers) {
                        minValue = Math.min(minValue, summarizer.getFuzzySet().membership(summarizer.getData().get(i)));
                    }
                    sum += minValue;
                }
                double average = sum / summarizers.getFirst().getData().size();
                return quantifier.getFuzzySet().membership(average);
            } else {
                double sum = 0.0;
                double size = summarizers.size();
                for (int i = 0; i < summarizers.getFirst().getData().size(); i++) {
                    double temp = 0.0;
                    for (Summarizer summarizer : summarizers) {
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
        List<Double> dataS = summarizers.getFirst().getData();

        double sum = 0.0;

        for (int i = 0; i < dataQ.size(); i++) {
            double muQ = qualifier.getFuzzySet().membership(dataQ.get(i));
            double muS = summarizers.getFirst().getFuzzySet().membership(dataS.get(i));
            sum += Math.min(muQ, muS);
        }

        if (sum == 0.0) return 0.0;

        return quantifier.getFuzzySet().membership(sum / qualifier.getFuzzySet().cardinality(dataS));
    }

    public double degreeOfImprecision() {
        double sum = 1.0;
        for (Summarizer value : summarizers) {
            sum *= value.getFuzzySet().degreeOfFuzziness(value.getData());
        }
        return 1 - (Math.pow(sum, 1.0 / summarizers.size()));
    }

    //TODO ZROBIC Z WIELOMA SUMARYZATORAMI JESZCZE
    public double degreeOfCovering() {
        double t = 0.0;
        double h = 0.0;
        if (qualifier != null) {
            for (int i = 0; i < summarizers.getFirst().getData().size(); i++) {
                if (summarizers.getFirst().getFuzzySet().contains(summarizers.getFirst().getData().get(i)) > 0.0 &&
                        qualifier.getFuzzySet().contains(qualifier.getData().get(i)) > 0) {
                    t += 1;
                }
                if (qualifier.getFuzzySet().contains(qualifier.getData().get(i)) > 0.0) {
                    h += 1;
                }
            }
        } else {
            t = summarizers.getFirst().getFuzzySet().support(summarizers.getFirst().getData()).size();
            h = summarizers.getFirst().getData().size();
        }

        return t / h;
    }

    public double degreeOfAppropriateness() {
        double sum = 1.0;
        for (Summarizer summarizer : summarizers) {
            double g = 0.0;
            for (Double value : summarizer.getData()) {
                if (summarizer.getFuzzySet().membership(value) > 0.0) {
                    g+= 1.0;
                }
            }
            sum *= (g * summarizer.getData().size());
        }
        return Math.abs(sum - degreeOfCovering());
    }

    public double lengthOfSummary() {
        return (2 * Math.pow(0.5, summarizers.size()));
    }

    //TODO TUTAJ JEST JESZCZE JAKIEŚ S*
    public double optimalSummary(double w1, double w2, double w3, double w4, double w5) {
        if (w1 + w2 + w3 + w4 + w5 != 1) {
            throw new IllegalArgumentException("Weights must sum to one.");
        }

        return (w1 * degreeOfTruth() + w2 * degreeOfImprecision() + w3 * degreeOfCovering() + w4 * degreeOfAppropriateness()
                + w5 * lengthOfSummary());
    }

    //TODO KURDE TO JEST JESZCZE DO ZROBIENIA
    public double degreeOfQuantifierImprecision() {
        return 1.0 - quantifier.getFuzzySet().support(summarizers.getFirst().getData()).size();
    }
    //TODO KURDE TO JEST JESZCZE DO ZROBIENIA
    public double degreeOfQuantifierCardinality() {
        return 1.0;
    }

    public double degreeOfSummarizerImprecision() {
        double sum = 1.0;
        for (Summarizer summarizer : summarizers) {
            sum *= (summarizer.getFuzzySet().fuzzyCardinality(summarizer.getData()) / summarizer.getData().size());
        }
        return 1 - (Math.pow(sum, 1.0 / summarizers.size()));
    }

    public double degreeOfQualifierImprecision() {
        if (qualifier == null) {
            return 0.0;
        }
        return 1.0 - qualifier.getFuzzySet().degreeOfFuzziness(qualifier.getData());
    }

    public double degreeOfQualifierCardinality() {
        if (qualifier == null) {
            return 0.0;
        }
        return 1.0 - (qualifier.getFuzzySet().fuzzyCardinality(qualifier.getData()) / qualifier.getData().size());
    }
}
