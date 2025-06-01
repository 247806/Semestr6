package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class CoValues {
    public static LinguisticVariable coQualityVariable = new LinguisticVariable("Zanieczyszczenie CO", 0.0, 2220.0);

    // normalne: 1 na [0, 200], opada 200-400
    public static LinguisticTerm normalne = new LinguisticTerm(
            "normalne", new TrapezoidalFunction(0, 0, 200, 400)
    );

    // niezdrowe: rośnie 200-400, 1 na [400, 800], opada 800-1000
    public static LinguisticTerm niezdrowe = new LinguisticTerm(
            "niezdrowe", new TrapezoidalFunction(200, 400, 800, 1000)
    );

    // niebezpieczne: rośnie 800-1000, 1 na [1000, 2220]
    public static LinguisticTerm niebezpieczne = new LinguisticTerm(
            "niebezpieczne", new TrapezoidalFunction(800, 1000, 2220, 2220)
    );
}
