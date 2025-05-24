package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quantifier {
    private String name;
    private FuzzySet fuzzySet;
    private LinguisticVariable linguisticVariable;
    private boolean isRelative;

    public Quantifier(String name, LinguisticVariable linguisticVariable, boolean isRelative) {
        this.name = name;
        this.linguisticVariable = linguisticVariable;
        this.fuzzySet = linguisticVariable.getTerms().get(name);
        this.isRelative = isRelative;
    }
}
