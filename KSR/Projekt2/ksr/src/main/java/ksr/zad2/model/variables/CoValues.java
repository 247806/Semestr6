package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class CoValues {
    public static LinguisticVariable coQualityVariable = new LinguisticVariable("Zanieczyszczenie CO", 0.0, 3000.0);

    // normalne: 1 na [0, 200], opada 200-400
    public static LinguisticTerm normalne = new LinguisticTerm(
            "normalne zanieczyszczenie CO2", new TrapezoidalFunction(0, 0.01, 200, 500)
    );

    public static LinguisticTerm wysokie = new LinguisticTerm(
            "wysokie zanieczyszczenie CO2", new TrapezoidalFunction(200, 500, 800, 1100)
    );

    // niezdrowe: rośnie 200-400, 1 na [400, 800], opada 800-1000
    public static LinguisticTerm niezdrowe = new LinguisticTerm(
            "niezdrowe zanieczyszczenie CO2", new TrapezoidalFunction(800, 1100, 1700, 2000)
    );

    // niebezpieczne: rośnie 800-1000, 1 na [1000, 2220]
    public static LinguisticTerm niebezpieczne = new LinguisticTerm(
            "niebezpieczne zanieczyszczenie CO2", new TrapezoidalFunction(1700, 2000, 3000, 3000.1)
    );
}
