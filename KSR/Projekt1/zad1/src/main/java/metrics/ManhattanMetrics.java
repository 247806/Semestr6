package metrics;

import loading.Article;

import java.util.List;

public class ManhattanMetrics implements Metrics {
    @Override
    public double calculate(Article article1, Article article2, NGramMethod nGramMethod) {
        double sum = 0.0;
        List<Object> features1 = article1.getFeatures();
        List<Object> features2 = article2.getFeatures();
        for (int i = 0; i < features1.size(); i++) {
            Object c1 = features1.get(i);
            Object c2 = features2.get(i);
            if (c1 == null || c2 == null) {
                sum+=1.0;
            } else if (c1 instanceof String && c2 instanceof String) {
                double similarity = nGramMethod.calculateNGramSimilarity((String) c1,(String) c2, 2, 4);
                sum+= Math.abs(1 - similarity);
            } else if (c1 instanceof List<?> list1 && c2 instanceof List<?> list2) {

                if (list1.isEmpty() || list2.isEmpty()) {
                    sum += 1.0;
                } else {
                    double totalSimilarity = 0.0;
                    int count = 0;

                    for (Object o1 : list1) {
                        for (Object o2 : list2) {
                            totalSimilarity += nGramMethod.calculateNGramSimilarity(
                                    o1.toString(), o2.toString(), 2, 4);
                            count++;
                        }
                    }

                    double avgSimilarity = totalSimilarity / count;
                    sum += Math.abs(1 - avgSimilarity);
                }
            } else {
                double value1 = Double.parseDouble(features1.get(i).toString());
                double value2 = Double.parseDouble(features2.get(i).toString());
                sum += Math.abs(value1 - value2);
            }
        }
        return sum;
    }
}
