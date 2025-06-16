package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

public class TimeValues {
    public static LinguisticVariable timeVariable = new LinguisticVariable("Pora dnia", 0.0, 24.0);

    public static LinguisticTerm nocna = new LinguisticTerm(
            "nocna godzina", new TrapezoidalFunction(21, 24, 0, 4, 7)
    );

    public static LinguisticTerm poranna = new LinguisticTerm(
            "poranna godzina", new TrapezoidalFunction(4, 7, 9, 12)
    );

    public static LinguisticTerm poludniowa = new LinguisticTerm(
            "południowa godzina", new TrapezoidalFunction(9, 12, 13, 16)
    );

    public static LinguisticTerm popoludniowa = new LinguisticTerm(
            "popołudniowa godzina", new TrapezoidalFunction(13, 16, 17, 20)
    );

    public static LinguisticTerm wieczorna = new LinguisticTerm(
            "wieczorna godzina", new TrapezoidalFunction(17, 20, 21, 24)
    );
}
