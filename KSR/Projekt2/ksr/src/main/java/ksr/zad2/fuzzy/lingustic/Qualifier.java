package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper=false)
public class Qualifier extends LinguisticTerm {
    public Qualifier(String name, FuzzySet fuzzySet, List<Double> data) {
        super(name, fuzzySet, data);
    }

}
