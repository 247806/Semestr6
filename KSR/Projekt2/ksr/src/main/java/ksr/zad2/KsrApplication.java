package ksr.zad2;

import ksr.zad2.fuzzy.lingustic.Quantifier;
import ksr.zad2.fuzzy.set.GaussianFunction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KsrApplication {

	public static void main(String[] args) {
//		SpringApplication.run(KsrApplication.class, args);
		// Przykładowe dane temperatury
		List<Double> daneTemperatury = Arrays.asList(
				-20.0, -5.0, 0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0,
				-10.0, 2.0, 8.0, 12.0, 18.0, 22.0, 28.0, 32.0, 40.0, 45.0
		);

		List<String> terminy = Arrays.asList(
				"bardzo zimno", "zimno", "umiarkowanie", "ciepło", "gorąco"
		);

		List<String> quantifiers = Arrays.asList(
				"prawie żaden", "trochę", "około połowy", "wiele", "prawie wszystkie"
		);

		Quantifier quantifier = new Quantifier(quantifiers.getFirst(), new GaussianFunction(0.0, 0.2));

		System.out.println(quantifier.getFuzzy().contains(0.2));

	}

}
