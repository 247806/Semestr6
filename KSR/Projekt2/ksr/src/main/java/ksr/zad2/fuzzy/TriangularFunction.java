package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TriangularFunction extends FuzzySet {
    private final double a;
    private final double b;
    private final double c;

    @Override
    public double membership(double x) {
        if (x < a || x > c) {
            return 0;
        } else if (x >= a && x <= b) {
            return (x - a) / (b - a);
        } else {
            return (c - x) / (c - b);
        }
    }
}
