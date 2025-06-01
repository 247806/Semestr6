package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;

import java.util.Arrays;
import java.util.List;

public class TempValues {
    public static LinguisticVariable tempVariable = new LinguisticVariable("Temperatura",-25.0, 50.0);;
    public static LinguisticTerm bardzoZimna = new LinguisticTerm("bardzo zimna", new TrapezoidalFunction( -26, -25, -15, -5));
    public static LinguisticTerm zimna = new LinguisticTerm("zimna", new TrapezoidalFunction(-15, -5, 0, 10));
    public static LinguisticTerm umiarkowana = new LinguisticTerm("umiarkowana", new TrapezoidalFunction(0, 10, 15, 25));
    public static LinguisticTerm ciepla = new LinguisticTerm("ciepła", new TrapezoidalFunction(15, 25, 30, 40));
    public static LinguisticTerm goraca = new LinguisticTerm("gorąca", new TrapezoidalFunction(30, 40, 50, 50));

}
