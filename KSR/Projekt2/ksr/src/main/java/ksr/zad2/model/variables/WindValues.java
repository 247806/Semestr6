package ksr.zad2.model.variables;

import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.LinguisticVariable;
import ksr.zad2.fuzzy.TrapezoidalFunction;
import ksr.zad2.fuzzy.TriangularFunction;

public class WindValues {
    public static LinguisticVariable windVariable = new LinguisticVariable("Wiatr", 3.0, 92);

    public static LinguisticTerm slaby = new LinguisticTerm(
            "słaby", new TriangularFunction(0, 0.01, 20)
    );

    public static LinguisticTerm umiarkowany = new LinguisticTerm(
            "umiarkowany", new TriangularFunction(0, 20, 40)
    );

    public static LinguisticTerm silny = new LinguisticTerm(
            "silny", new TriangularFunction(20, 40, 60)
    );

    public static LinguisticTerm bardzoSilny = new LinguisticTerm(
            "bardzo_silny", new TriangularFunction(40, 60, 80)
    );

    public static LinguisticTerm gwaltowny = new LinguisticTerm(
            "gwałtowny", new TrapezoidalFunction(60, 80, 92, 92)  // trójkąt z prawej ramki ściętej
    );

}
