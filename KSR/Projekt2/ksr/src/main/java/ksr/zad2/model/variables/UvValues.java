package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class UvValues {
    public static LinguisticVariable uvIndexVariable = new LinguisticVariable("Promieniowanie UV", 0.0, 16.0);

    public static LinguisticTerm niskie = new LinguisticTerm(
            "niskie", new TrapezoidalFunction(0, 0.01, 2, 3)
    );

    public static LinguisticTerm umiarkowane = new LinguisticTerm(
            "umiarkowane", new TrapezoidalFunction(2, 3, 5, 6)
    );

    public static LinguisticTerm wysokie = new LinguisticTerm(
            "wysokie", new TrapezoidalFunction(5, 6, 7, 8)
    );

    public static LinguisticTerm bardzoWysokie = new LinguisticTerm(
            "bardzo wysokie", new TrapezoidalFunction(7, 8, 10, 11)
    );

    public static LinguisticTerm ekstremalne = new LinguisticTerm(
            "ekstremalne", new TrapezoidalFunction(10, 11, 16, 16.1)
    );
}
