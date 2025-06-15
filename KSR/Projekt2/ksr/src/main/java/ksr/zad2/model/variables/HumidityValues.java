package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;
import ksr.zad2.fuzzy.TriangularFunction;

public class HumidityValues {
    public static LinguisticVariable humidityVariable = new LinguisticVariable("Wilgotność powietrza", 2.0, 100.0);

    public static LinguisticTerm suche = new LinguisticTerm(
            "suche", new TrapezoidalFunction(0, 0.01, 10, 50)
    );

    public static LinguisticTerm umiarkowane = new LinguisticTerm(
            "umiarkowane", new TriangularFunction(10, 50, 80)
    );

    public static LinguisticTerm wilgotne = new LinguisticTerm(
            "wilgotne", new TrapezoidalFunction(50, 80, 100, 100.1)
    );
}
