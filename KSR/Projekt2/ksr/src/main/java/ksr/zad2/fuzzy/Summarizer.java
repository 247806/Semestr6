package ksr.zad2.fuzzy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class Summarizer extends LinguisticTerm {
    public Summarizer(String name, LinguisticVariable variable, List<Double> data) {
        super(name, variable, data);
    }

}
