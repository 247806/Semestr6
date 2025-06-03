package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class VisibilityValues {
    public static LinguisticVariable visibilityVariable = new LinguisticVariable("Stopień widoczności", 0.0, 32.0);


    public static LinguisticTerm slaba = new LinguisticTerm(
            "słaba", new TrapezoidalFunction(0, 0.01, 4, 8)
    );

   public static LinguisticTerm umiarkowana = new LinguisticTerm(
            "umiarkowana", new TrapezoidalFunction(4, 8, 12, 16)
    );


    public static LinguisticTerm dobra = new LinguisticTerm(
            "dobra", new TrapezoidalFunction(12, 16, 24, 28)
    );

    public static LinguisticTerm bardzoDobra = new LinguisticTerm(
            "bardzo dobra", new TrapezoidalFunction(24, 28, 32, 32)
    );
}
