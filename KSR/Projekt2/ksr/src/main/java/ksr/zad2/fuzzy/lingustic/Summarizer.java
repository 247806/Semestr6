package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import ksr.zad2.fuzzy.set.TriangularFunction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Summarizer {
    private String name;
    private FuzzySet fuzzySet;
    private List<Double> data;
    private LinguisticVariable linguisticVariable;
}
