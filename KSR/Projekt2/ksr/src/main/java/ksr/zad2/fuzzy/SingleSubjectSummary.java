package ksr.zad2.fuzzy;

import lombok.Data;


import java.util.List;

@Data
public class SingleSubjectSummary {
    private Quantifier quantifier;
    private List<LinguisticTerm> summarizers;
    //TODO A MOZE BYC LISTA KWALIFIKATORÓW?
    private LinguisticTerm qualifier;
    private double t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11;
    private double optimal;

    public SingleSubjectSummary(Quantifier quantifier, List<LinguisticTerm> summarizers) {
        this.quantifier = quantifier;
        this.summarizers = summarizers;
    }

    public SingleSubjectSummary(Quantifier quantifier, List<LinguisticTerm> summarizers, LinguisticTerm qualifier) {
        this.quantifier = quantifier;
        this.summarizers = summarizers;
        this.qualifier = qualifier;
    }

    public String summarization() {
        StringBuilder summaryText = new StringBuilder(quantifier.getName() + " pomiarów ");
        if (qualifier != null) {
            summaryText.append("będący/mający ").append(qualifier.getName());
        }
        for (int i = 0; i < summarizers.size(); i++) {
            summaryText.append(" jest/ma ").append(summarizers.get(i).getName());
            if (i != summarizers.size() - 1) {
                summaryText.append(" i ");
            }
        }

        this.processQualityMeasurements();
        return summaryText.toString();
    }

    public void processQualityMeasurements() {
        this.t1 = degreeOfTruth();
        this.t2 = degreeOfImprecision();
        this.t3 = degreeOfCovering();
        this.t4 = degreeOfAppropriateness();
        this.t5 = lengthOfSummary();
        this.t6 = degreeOfQuantifierImprecision();
        this.t7 = degreeOfQuantifierCardinality();
        this.t8 = degreeOfSummarizerImprecision();
        this.t9 = degreeOfQualifierImprecision();
        this.t10 = degreeOfQualifierCardinality();
        this.t11 = T11();
    }

    public void print() {
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
        System.out.println(t5);
        System.out.println(t6);
        System.out.println(t7);
        System.out.println(t8);
        System.out.println(t9);
        System.out.println(t10);
        System.out.println(t11);
        System.out.println(optimalSummary(List.of(0.20, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08),
                List.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)));
    }

    public double degreeOfTruth() {
        if (qualifier != null) {
            return this.degreeOfTruthWithQualifier();
        } else {
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
                        for (LinguisticTerm summarizer : summarizers) {
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
                        for (LinguisticTerm summarizer : summarizers) {
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
    }

    public double degreeOfTruthWithQualifier() {
        if (qualifier != null && quantifier.isRelative()) {
            List<Double> dataQ = qualifier.getData();

            double sum = 0.0;
            double temp = 0.0;

            for (int i = 0; i < dataQ.size(); i++) {
                temp+= qualifier.getFuzzySet().membership(dataQ.get(i));
                double minValue = qualifier.getFuzzySet().membership(dataQ.get(i));
                for (LinguisticTerm summarizer : summarizers) {
                    minValue = Math.min(minValue, summarizer.getFuzzySet().membership(summarizer.getData().get(i)));
                }
                sum += minValue;
            }

            if (sum == 0.0 || temp == 0.0) return 0.0;

            return quantifier.getFuzzySet().membership(sum / temp);
        } else {
            return 0.0;
        }

    }

    public double degreeOfImprecision() {
        double sum = 1.0;
        for (LinguisticTerm value : summarizers) {
            sum *= value.getFuzzySet().degreeOfFuzziness(value.getData());
        }
        return 1 - (Math.pow(sum, 1.0 / (double) summarizers.size()));
    }

    public double degreeOfCovering() {
        double t = 0.0;
        double h = 0.0;
        if (qualifier != null) {
            for (int i = 0; i < summarizers.getFirst().getData().size(); i++) {
                if (qualifier.getFuzzySet().membership(qualifier.getData().get(i)) <= 0.0) {
                    continue;
                } else {
                    h += 1.0;
                }

                boolean correct = true;
                for (LinguisticTerm summarizer : summarizers) {
                    if (summarizer.getFuzzySet().membership(summarizer.getData().get(i)) <= 0.0) {
                        correct = false;
                        break;
                    }
                }
                if (correct) {
                    t += 1.0;
                }
            }
        } else {
            for (int i = 0; i < summarizers.getFirst().getData().size(); i++) {
                boolean correct = true;
                for (LinguisticTerm summarizer : summarizers) {
                    if (summarizer.getFuzzySet().membership(summarizer.getData().get(i)) <= 0.0) {
                        correct = false;
                        break;
                    }
                }
                if (correct) {
                    t += 1.0;
                }
            }

            h = summarizers.getFirst().getData().size();
        }

        if (h == 0.0) {
            return 0.0;
        }

        return t / h;
    }

    public double degreeOfAppropriateness() {
        double sum = 1.0;
        for (LinguisticTerm summarizer : summarizers) {
            double g = 0.0;
            for (Double value : summarizer.getData()) {
                if (summarizer.getFuzzySet().membership(value) > 0.0) {
                    g += 1.0;
                }
            }
            sum *= (g / summarizer.getData().size());
        }
        return Math.abs(sum - degreeOfCovering());
    }

    public double lengthOfSummary() {
        return (2 * Math.pow(0.5, summarizers.size()));
    }

    //TODO TEGO TO JA NIE JESTEM PEWIEN
    public double degreeOfQuantifierImprecision() {
        double result;
        if (quantifier.isRelative()) {
            result = quantifier.getRightLimit() - quantifier.getLeftLimit();
        } else {
            result = (quantifier.getRightLimit() - quantifier.getLeftLimit()) / summarizers.getFirst().getData().size();
        }

        return 1.0 - result;
    }
    //TODO NIE WIEM CZY TEGO NIE TRZEBA LICZYC JAKO POLE POD WYKRESEM
    public double degreeOfQuantifierCardinality() {
        if (quantifier.isRelative()) {
            double step = 0.01;
            double sum = 0.0;
            for (double i = quantifier.getLeftLimit(); i <= quantifier.getRightLimit(); i += step) {
                sum += quantifier.getFuzzySet().membership(i);
            }
            return 1.0 - (sum / ((quantifier.getRightLimit() - quantifier.getLeftLimit()) / step));
        } else {
            double sum = 0.0;
            for (double object : summarizers.getFirst().getData()) {
                if (quantifier.getFuzzySet().membership(object) > 0.0) {
                    sum += 1.0;
                }
            }
            return 1.0 - (sum / summarizers.getFirst().getData().size());
        }
    }

    //TODO PRZESTRZEŃ ROZWAŻAŃ W TYM WYPADKU CHYBA NIE JEST GĘSTA TO MOZNA JA OKRESLIC JAKO ILOSC ELEMENTÓW
    public double degreeOfSummarizerImprecision() {
        double sum = 1.0;
        if (quantifier.isRelative()) {
            for (LinguisticTerm summarizer : summarizers) {
                sum *= (summarizer.getFuzzySet().fuzzyCardinality(summarizer.getData()) / summarizer.getData().size());
            }
        } else {
            for (LinguisticTerm summarizer : summarizers) {
                sum *= ((double) summarizer.getFuzzySet().cardinality(summarizer.getData()) / summarizer.getData().size());
            }
        }

        return 1 - (Math.pow(sum, 1.0 / summarizers.size()));
    }

    //TODO JESZCZE JEST WZOR Z WIELOMA KWALIFIKATORAMI
    public double degreeOfQualifierImprecision() {
        if (qualifier == null) {
            return 0.0;
        }
        return 1.0 - qualifier.getFuzzySet().degreeOfFuzziness(qualifier.getData());
    }

    //TODO UWAGI JAK W przypadku degreeOfSummarizerImprecision
    //TODO JESZCZE JEST WZOR Z WIELOMA KWALIFIKATORAMI
    public double degreeOfQualifierCardinality() {
        if (qualifier == null) {
            return 0.0;
        }
        return 1.0 - (qualifier.getFuzzySet().fuzzyCardinality(qualifier.getData()) / qualifier.getData().size());
    }

    public double T11() {
        if (qualifier != null) {
            return 2.0 * Math.pow(0.5, 1.0);
        } else {
            return 0.0;
        }

    }

    public double optimalSummary(List<Double> weights, List<Double> measurements) {
        if (weights.stream().mapToDouble(Double::doubleValue).sum() != 1.0) {
            throw new IllegalArgumentException("Weights must sum to 1.0");
        }
        double result = 0.0;

        for (int i = 0; i < weights.size(); i++) {
            result += weights.get(i) * measurements.get(i);
        }

        return result;

    }
}
