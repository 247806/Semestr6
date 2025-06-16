package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class NoValues {
    public static LinguisticVariable no2QualityVariable = new LinguisticVariable("Zanieczyszczenie NO2", 0.0, 428.0);

    // normalne: 1 na [0, 50], opada 50-100
    public static LinguisticTerm normalne = new LinguisticTerm(
            "normalne zanieczyszczenie NO2", new TrapezoidalFunction(0, 0.01, 50, 100)
    );

    // niezdrowe: rośnie 50-100, 1 na [100, 200], opada 200-250
    public static LinguisticTerm niezdrowe = new LinguisticTerm(
            "niezdrowe zanieczyszczenie NO2", new TrapezoidalFunction(50, 100, 160, 210)
    );

    // niebezpieczne: rośnie 200-250, 1 na [250, 428]
    public static LinguisticTerm niebezpieczne = new LinguisticTerm(
            "niebezpieczne zanieczyszczenie NO2", new TrapezoidalFunction(160, 210, 261, 261.1)
    );
}
