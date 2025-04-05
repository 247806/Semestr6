package extraction;

import java.util.ArrayList;
import java.util.List;

public class Normalization {
    private final List<Double> minValues = new ArrayList<>(List.of(
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            Double.MAX_VALUE
    ));

    private final List<Double> maxValues = new ArrayList<>(List.of(
            Double.MIN_VALUE,
            Double.MIN_VALUE,
            Double.MIN_VALUE,
            Double.MIN_VALUE,
            Double.MIN_VALUE,
            Double.MIN_VALUE,
            Double.MIN_VALUE));

    public void preprocess(List<List<Object>> features) {
        for (List<Object> feature : features) {
            int counter = 0;
            for (int i = 0; i < feature.size(); i++) {
                if (feature.get(i) instanceof Number) {
                    double value = ((Number) feature.get(i)).doubleValue();

                    minValues.set(counter, Math.min(minValues.get(counter), value));
                    maxValues.set(counter, Math.max(maxValues.get(counter), value));

                    counter++;
                }
            }
        }
    }

    public List <Object> normalize(List <Object> feature) {
        List <Object> normalizedFeature = new ArrayList<>(feature);
        int counter = 0;
        for (int i = 0; i < feature.size(); i++) {
            if (feature.get(i) instanceof Number) {
                double value = ((Number) feature.get(i)).doubleValue();
                double minValue = minValues.get(counter);
                double maxValue = maxValues.get(counter);

                double normalizedValue;
                if (maxValue != minValue) {
                    normalizedValue = (value - minValue) / (maxValue - minValue);
                } else {
                    normalizedValue = 0.0;
                }

                normalizedFeature.set(i, normalizedValue);
                counter++;
            }

        }
        return normalizedFeature;
    }
}
