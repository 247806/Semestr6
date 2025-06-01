package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.GaussianFunction;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.Quantifier;
import lombok.Data;

@Data
public class QuantifierValues {
    public static LinguisticVariable linguisticVariableQ = new LinguisticVariable("Przynależność",0.0, 1.0);
    public static Quantifier Q1 = new Quantifier("prawie żaden", new GaussianFunction(0.0, 0.06), true, 0.0, 0.2);
    public static Quantifier Q2 = new Quantifier("trochę", new GaussianFunction(0.3, 0.06), true, 0.1, 0.5);
    public static Quantifier Q3 = new Quantifier("około połowy", new GaussianFunction(0.5, 0.06), true, 0.3, 0.7);
    public static Quantifier Q4 = new Quantifier("wiele", new GaussianFunction(0.7, 0.06), true, 0.5, 0.9);
    public static Quantifier Q5 = new Quantifier("prawie wszystkie", new GaussianFunction(1.0, 0.06), true, 0.8, 1.0);

}
