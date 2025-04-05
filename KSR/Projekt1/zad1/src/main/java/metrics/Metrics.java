package metrics;

import loading.Article;

public interface Metrics {
    double calculate(Article article1, Article article2, NGramMethod nGramMethod);
}
