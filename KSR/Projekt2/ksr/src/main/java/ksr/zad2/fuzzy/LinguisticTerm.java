package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinguisticTerm {
    private String name;
    private FuzzySet fuzzySet;
//    private LinguisticVariable linguisticVariable;
    private List<Double> data;

    public LinguisticTerm(String name, FuzzySet fuzzySet) {
        this.name = name;
//        this.data = data;
//        this.linguisticVariable = linguisticVariable;
        this.fuzzySet = fuzzySet;
    }

}

