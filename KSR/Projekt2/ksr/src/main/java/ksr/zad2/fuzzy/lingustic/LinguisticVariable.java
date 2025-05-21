package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LinguisticVariable {
    private String name;
    private List<String> terms;
    private Map<String, FuzzySet> fuzzySets;
    private List<FuzzySet> sets;
    private double min;
    private double max;

    public LinguisticVariable(String name, List<String> terms, List<FuzzySet> sets, double min, double max) {
        this.name = name;
        this.terms = terms;
        this.sets = sets;
        this.min = min;
        this.max = max;
    }

}
