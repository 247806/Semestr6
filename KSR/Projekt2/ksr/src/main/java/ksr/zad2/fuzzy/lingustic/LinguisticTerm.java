package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class LinguisticTerm {
    private String name;
    private FuzzySet fuzzySet;
    private LinguisticVariable linguisticVariable;
    private List<Double> data;

    public LinguisticTerm(String name, LinguisticVariable linguisticVariable, List<Double> data) {
        this.name = name;
        this.data = data;
        this.linguisticVariable = linguisticVariable;
        this.fuzzySet = linguisticVariable.getTerms().get(name);
    }

}

