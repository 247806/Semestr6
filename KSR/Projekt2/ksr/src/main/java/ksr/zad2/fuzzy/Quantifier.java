package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quantifier {
    private String name;
    private FuzzySet fuzzySet;
    private boolean isRelative;

    public Quantifier(String name, FuzzySet fuzzySet, boolean isRelative) {
        this.name = name;
        this.fuzzySet = fuzzySet;
        this.isRelative = isRelative;
    }
}
