package ksr.zad2.fuzzy.lingustic;

import ksr.zad2.fuzzy.set.FuzzySet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LinguisticVariable {
    private String name;
    private List<String> terms;
    private double min;
    private double max;

}
