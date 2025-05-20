package ksr.zad2.fuzzy.set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TriangularFunction implements FuzzySet {
    private final double a;
    private final double b;
    private final double c;

    @Override
    public double contains(double x) {
        if (x < a || x > c) {
            return 0;
        } else if (x >= a && x <= b) {
            return (x - a) / (b - a);
        } else {
            return (c - x) / (c - b);
        }
    }
}
