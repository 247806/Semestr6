package ksr.zad2.fuzzy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper=false)
public class Qualifier extends LinguisticTerm {
    public Qualifier(String name, LinguisticVariable variable, List<Double> data) {
        super(name, variable, data);
    }

}
