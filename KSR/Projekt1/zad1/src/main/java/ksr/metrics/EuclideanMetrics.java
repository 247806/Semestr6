package ksr.metrics;

import ksr.loading.Article;

import java.util.List;

public class EuclideanMetrics implements Metrics {
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
                sum+= Math.pow(1 - similarity, 2);
            } else if (c1 instanceof List<?> list1 && c2 instanceof List<?> list2) {

                if (list1.isEmpty() || list2.isEmpty()) {
                    sum += 1.0;
                } else {
                    double totalSimilarity = 0.0;
                    List<?> bigger = list1.size() > list2.size() ? list1 : list2;
                    List<?> smaller = list1.size() > list2.size() ? list2 : list1;

                    double count = Math.max(list1.size(), list2.size());
                    for (Object o1 : bigger) {
                        double temp = 0.0;
                        for (Object o2 : smaller) {
                            double temp2 = nGramMethod.calculateNGramSimilarity(
                                    o1.toString(), o2.toString(), 2, 4);
                            if (temp2 > temp) {
                                temp = temp2;
                            }
                        }
                        totalSimilarity += temp;
                    }

                    double avgSimilarity = totalSimilarity / count;
                    sum += Math.pow(1 - avgSimilarity, 2);
                }
            }  else {
                double value1 = Double.parseDouble(features1.get(i).toString());
                double value2 = Double.parseDouble(features2.get(i).toString());
                sum += Math.pow(value1 - value2, 2);
            }
        }
        return Math.sqrt(sum);
    }
}
