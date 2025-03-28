package extraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Normalization {
    private final Map<String, Double> minValues = new HashMap<>(Map.of(
            "keywords_in_first_3_sentences", Double.MAX_VALUE,
            "relative_keyword_count", Double.MAX_VALUE,
            "keyword_count", Double.MAX_VALUE,
            "length", Double.MAX_VALUE,
            "avg_word_length", Double.MAX_VALUE,
            "unique_word_count", Double.MAX_VALUE,
            "capitalized_word_count", Double.MAX_VALUE));

    private final Map<String, Double> maxValues = new HashMap<>(Map.of(
            "keywords_in_first_3_sentences", Double.MIN_VALUE,
            "relative_keyword_count", Double.MIN_VALUE,
            "keyword_count", Double.MIN_VALUE,
            "length", Double.MIN_VALUE,
            "avg_word_length", Double.MIN_VALUE,
            "unique_word_count", Double.MIN_VALUE,
            "capitalized_word_count", Double.MIN_VALUE));

    public void preprocess(List<Map<String, Object>> features) {
        for (Map<String, Object> feature : features) {
            for (Map.Entry<String, Object> entry : feature.entrySet()) {
                if (entry.getValue() instanceof Number) {
                    double value = ((Number) entry.getValue()).doubleValue();

                    minValues.put(entry.getKey(), Math.min(minValues.get(entry.getKey()), value));
                    maxValues.put(entry.getKey(), Math.max(maxValues.get(entry.getKey()), value));
                }
            }
        }
    }

    public Map<String, Object> normalize(Map<String, Object> feature) {
        Map<String, Object> normalizedFeature = new HashMap<>(feature);

        for (Map.Entry<String, Object> entry : feature.entrySet()) {
            if (entry.getValue() instanceof Number && minValues.containsKey(entry.getKey())) {
                double value = ((Number) entry.getValue()).doubleValue();
                double minValue = minValues.get(entry.getKey());
                double maxValue = maxValues.get(entry.getKey());

                double normalizedValue;
                if (maxValue != minValue) {
                    normalizedValue = (value - minValue) / (maxValue - minValue);
                } else {
                    normalizedValue = 0.0;
                }

                normalizedFeature.put(entry.getKey(), normalizedValue);
            }
        }
        return normalizedFeature;
    }
}
