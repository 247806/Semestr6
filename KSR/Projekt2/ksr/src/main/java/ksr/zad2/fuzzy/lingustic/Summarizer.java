package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import ksr.zad2.fuzzy.set.TriangularFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class Summarizer extends LinguisticTerm {
    public Summarizer(String name, FuzzySet fuzzySet, List<Double> data) {
        super(name, fuzzySet, data);
    }

}
