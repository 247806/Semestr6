package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class PressureValues {
    public static LinguisticVariable pressureVariable = new LinguisticVariable("Ciśnienie", 947.0, 1052.0);

    public static LinguisticTerm niskie = new LinguisticTerm(
            "niskie ciśnienie", new TrapezoidalFunction(963.9, 964, 970, 1000)
    );

    public static LinguisticTerm normalne = new LinguisticTerm(
            "normalne ciśnienie", new TrapezoidalFunction(970, 1000, 1020, 1040)
    );

    public static LinguisticTerm wysokie = new LinguisticTerm(
            "wysokie ciśnienie", new TrapezoidalFunction(1020, 1040, 1050, 1052)
    );
}
