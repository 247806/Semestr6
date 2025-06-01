package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quantifier {
    private String name;
    private FuzzySet fuzzySet;
    private boolean isRelative;
    private double leftLimit;
    private double rightLimit;

}
