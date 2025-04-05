package metrics;

import loading.Article;

import java.util.List;

public class EuclideanMetrics implements Metrics {
    @Override
    public double calculate(Article article1, Article article2, NGramMethod nGramMethod) {
        double sum = 0.0;
        List<Object> features1 = article1.getFeatures();
        List<Object> features2 = article2.getFeatures();
        int counter = 1;
        for (int i = 0; i < features1.size(); i++) {
            Object c1 = features1.get(i);
            Object c2 = features2.get(i);
            System.out.println("CECHA NUMER " + counter);
            if (c1 == null || c2 == null) {
                sum+=1.0;
//                System.out.println("MAMY NULLA");
                System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
            } else if (c1 instanceof String && c2 instanceof String) {
                double similarity = nGramMethod.calculateNGramSimilarity((String) c1,(String) c2, 2, 4);
                System.out.println(similarity);
                sum+= Math.pow(1 - similarity, 2);
//                System.out.println("MAMY STRINGA");
                System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
            } else if (c1 instanceof List<?> list1 && c2 instanceof List<?> list2) {

                if (list1.isEmpty() || list2.isEmpty()) {
                    sum += 1.0;
//                    System.out.println("ZBIÓR STRINGÓW");
                    System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
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
                    sum += Math.pow(1 - avgSimilarity, 2);
//                    System.out.println("ZBIÓR STRINGÓW");
                    System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
                }
            }  // Object[] vs Object[]
            else if (c1 instanceof Object[] arr1 && c2 instanceof Object[] arr2) {

                if (arr1.length == 0 || arr2.length == 0) {
                    sum += 1;
//                    System.out.println("TABLICE");
                    System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
                } else {
                    double totalSimilarity = 0.0;
                    int count = 0;

                    for (Object o1 : arr1) {
                        for (Object o2 : arr2) {
                            totalSimilarity += nGramMethod.calculateNGramSimilarity(
                                    o1.toString(), o2.toString(), 2, 4);
                            count++;
                        }
                    }

                    double avgSimilarity = totalSimilarity / count;
                    sum += Math.pow(1 - avgSimilarity, 2);
//                    System.out.println("TABLICE");
                    System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
                }
            } else {
                double value1 = Double.parseDouble(features1.get(i).toString());
                double value2 = Double.parseDouble(features2.get(i).toString());
                sum += Math.pow(value1 - value2, 2);
//                System.out.println("MAMY DOUBLEA");
                System.out.println("cecha 1: " + c1 + " cecha 2: " + c2 + " suma: " + sum);
            }
            counter++;
        }
        return Math.sqrt(sum);
    }
}
