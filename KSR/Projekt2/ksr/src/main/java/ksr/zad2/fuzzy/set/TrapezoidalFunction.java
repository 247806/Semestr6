package ksr.zad2.fuzzy.set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrapezoidalFunction implements FuzzySet {
    private final double a;
    private final double b;
    private final double c;
    private final double d;


    @Override
    public double contains(double x) {
        if (x < a || x > d) {
            return 0;
        } else if (x >= a && x <= b) {
            return (x - a) / (b - a);
        } else if (x >= b && x <= c) {
            return 1;
        } else {
            return (d - x) / (d - c);
        }
    }
}
