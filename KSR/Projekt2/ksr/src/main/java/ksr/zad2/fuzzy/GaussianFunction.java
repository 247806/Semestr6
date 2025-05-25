package ksr.zad2.fuzzy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GaussianFunction extends FuzzySet {
    private final double center;
    private final double sigma;

    @Override
    public double membership(double x) {
        return Math.exp(-0.5 * (((x - center) / (sigma)) * ((x - center) / (sigma))));
    }

}
