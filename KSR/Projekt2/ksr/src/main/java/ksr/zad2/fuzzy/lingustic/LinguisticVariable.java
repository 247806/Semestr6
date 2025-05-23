package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class LinguisticVariable {
    private String name;
    private Map<String, FuzzySet> terms = new LinkedHashMap<>();
    private double min;
    private double max;

    public LinguisticVariable(String name, double min, double max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public void addTerm(String term, FuzzySet function) {
        terms.put(term, function);
    }


}
