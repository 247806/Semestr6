package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;


public class TrapezoidalFunction extends FuzzySet {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private double e = 0.0;

    public TrapezoidalFunction(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public TrapezoidalFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double membership(double x) {
        if (e != 0) {
            if (x > e || x < a) {
                return 0;
            } else if (x >= a && x <= b) {
                return (x - a) / (b - a);
            } else if (x >= c && x <= d) {
                return 1;
            } else {
                return (e - x) / (e - d);
            }
        } else {
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
}
