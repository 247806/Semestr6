package ksr.metrics;

import ksr.loading.Article;

public interface Metrics {
    double calculate(Article article1, Article article2, NGramMethod nGramMethod);
}
