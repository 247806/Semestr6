package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class AirValues {
    public static LinguisticVariable airQuality = new LinguisticVariable("Jakość powietrza", 1.0, 10.0);

    public static LinguisticTerm bardzoDobra = new LinguisticTerm(
            "bardzo dobra jakość powietrza", new TrapezoidalFunction(1, 1.01, 2, 3)
    );

    public static LinguisticTerm dobra = new LinguisticTerm(
            "dobra jakość powietrza", new TrapezoidalFunction(2, 3, 4, 5)
    );

    public static LinguisticTerm umiarkowana = new LinguisticTerm(
            "umiarkowana jakość powietrza", new TrapezoidalFunction(4, 5, 6, 7)
    );

    public static LinguisticTerm zla = new LinguisticTerm(
            "zła jakość powietrza", new TrapezoidalFunction(6, 7, 8, 9)
    );

    public static LinguisticTerm bardzoZla = new LinguisticTerm(
            "bardzo zła jakość powietrza", new TrapezoidalFunction(8, 9, 10, 10.1)
    );
}
