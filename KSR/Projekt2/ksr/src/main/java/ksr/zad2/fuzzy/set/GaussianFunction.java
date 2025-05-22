package ksr.zad2.fuzzy.set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GaussianFunction extends FuzzySet {
    private final double center;
    private final double width;

    @Override
    public double membership(double x) {
        return Math.exp(-0.5 * (((x - center) / (width / 3)) * ((x - center) / (width / 3))));
    }

}
