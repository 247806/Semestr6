package ksr.zad2.fuzzy;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public abstract class FuzzySet {
    public abstract double membership(double x);

    /**
     * Kardynalność klasyczna – liczba elementów, dla których przynależność > 0
     */
    public long cardinality(List<Double> values) {
        return values.stream()
                .filter(x -> membership(x) > 0.0)
                .count();
    }

    /**
     * Kardynalność rozmyta – suma przynależności wszystkich elementów
     */
    public double fuzzyCardinality(List<Double> universe) {
        return universe.stream()
                .mapToDouble(this::contains)
                .sum();
    }

    /**
     * Nośnik zbioru rozmytego – elementy, których przynależność > 0
     */
    public List<Double> support(List<Double> universe) {
        return universe.stream()
                .filter(x -> membership(x) > 0.0)
                .collect(Collectors.toList());
    }

    /**
     * Przekrój alfa – elementy, których przynależność >= a
     */
    public List<Double> alphaCut(List<Double> universe, double a) {
        return universe.stream()
                .filter(x -> membership(x) >= a)
                .collect(Collectors.toList());
    }

    /**
     * Stopień rozmycia – stosunek liczby elementów nośnika do liczby wszystkich elementów
     */
    public double degreeOfFuzziness(List<Double> universe) {
        return (double) support(universe).size() / (double) universe.size();
    }

    public double contains(double x) {
        return membership(x);
    }

}
