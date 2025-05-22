package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quantifier {
    private String name;
    private FuzzySet fuzzySet;
    private String type;
    private LinguisticVariable variable;
}
