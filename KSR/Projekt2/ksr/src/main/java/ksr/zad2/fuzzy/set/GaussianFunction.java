package ksr.zad2.fuzzy.set;

import lombok.Data;

@Data
public class GaussianFunction implements FuzzySet {
    private final double center;
    private final double width;

    public GaussianFunction(double center, double width) {
        this.center = center;
        this.width = width;
    }

    @Override
    public double contains(double x) {
        return Math.exp(-(center - x) * (center - x) / (2 * width * width));
    }

}
