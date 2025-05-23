package ksr.zad2.fuzzy.lingustic;

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

    public String singleSummarization() {
        double truth = degreeOfTruth();
        return quantifier.getName() + " measurements ma " + summarizer.getFirst().getName() + " temperaturę " + " [" +
                truth + "] ";
    }

    public String doubleSummarization() {
        double truth = degreeOfTruth();
        return quantifier.getName() + " measurements ma " + summarizer.getFirst().getName() + " temperaturę " +
                "oraz " + summarizer.get(1).getName() + " wilgotność " + " [" +
                truth + "] ";
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

            if (!quantifier.isRelative()) {
                return quantifier.getFuzzySet().membership(sum);
            } else {
                return quantifier.getFuzzySet().membership(average);
            }
        }
    }

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
