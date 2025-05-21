package ksr.zad2.fuzzy.set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GaussianFunction implements FuzzySet {
    private final double center;
    private final double width;

    @Override
    public double contains(double x) {
        return Math.exp(-0.5 * (((x - center) / (width / 3)) * ((x - center) / (width / 3))));
    }

}
