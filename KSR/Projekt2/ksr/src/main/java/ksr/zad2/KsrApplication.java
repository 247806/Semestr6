package ksr.zad2;

import ksr.zad2.fuzzy.lingustic.*;
import ksr.zad2.fuzzy.set.GaussianFunction;
import ksr.zad2.fuzzy.set.TrapezoidalFunction;
import ksr.zad2.fuzzy.set.TriangularFunction;
import ksr.zad2.model.Measurements;
import ksr.zad2.repository.MeasurementsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KsrApplication implements CommandLineRunner {

	private final MeasurementsRepository measurementsRepository;

	public KsrApplication(MeasurementsRepository measurementsRepository) {
		this.measurementsRepository = measurementsRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(KsrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Hello World!");

		// Wywołanie metody repozytorium
		List<Measurements> allMeasurements = measurementsRepository.findAll();

		List<Double> temperatures = new ArrayList<>();
		for (Measurements measurement : allMeasurements) {
			temperatures.add((double) measurement.getTemperature_celsius());
		}

		List<Double> humidities = new ArrayList<>();
		for (Measurements measurement : allMeasurements) {
			humidities.add((double) measurement.getHumidity());
		}

		List<LocalDateTime> dates = new ArrayList<>();
		for (Measurements measurement : allMeasurements) {
			dates.add(measurement.getLast_updated());
		}

		List<Double> hours = new ArrayList<>();
		for (LocalDateTime date : dates) {
			hours.add((double) date.getHour() + date.getMinute() / 60.0);
		}

		List<String> terminy1 = Arrays.asList(
				"bardzo zimno", "zimno", "umiarkowanie", "ciepło", "gorąco"
		);

		List<String> quantifiers = Arrays.asList(
				"prawie żaden", "trochę", "około połowy", "wiele", "prawie wszystkie"
		);

		List<String> terminy2 = Arrays.asList(
				"suchą", "umiarkowaną", "wilgotną"
		);

		List<String> terminy3 = Arrays.asList(
				"nocna", "poranna", "południowa", "popołudniowa", "wieczorna"
		);

		LinguisticVariable linguisticVariable = new LinguisticVariable("Temperatura", terminy1, -30.0, 50.0);
		LinguisticVariable linguisticVariable2 = new LinguisticVariable("Wilgotność", terminy2, 0.0, 100.0);
		LinguisticVariable linguisticVariable3 = new LinguisticVariable("Przynależność", quantifiers, 0.0, 1.0);
		LinguisticVariable linguisticVariable4 = new LinguisticVariable("Pora dnia", terminy3, 0.0, 24.0);

		Quantifier quantifier = new Quantifier(quantifiers.get(4), new GaussianFunction(1.0, 0.2), "RELATIVE", linguisticVariable3);
		Summarizer summarizer1 = new Summarizer(terminy1.get(4), new TrapezoidalFunction(25, 35, 50, 51), temperatures, linguisticVariable);
		Summarizer summarizer2 = new Summarizer(terminy2.get(2), new TriangularFunction(60, 100, 101), humidities, linguisticVariable2);
		Qualifier qualifier = new Qualifier(terminy3.get(4), new TrapezoidalFunction(19, 20, 22, 24), hours, linguisticVariable4);

		Summary summary = new Summary(quantifier, List.of(summarizer1));
		System.out.println(summary.singleSummarization());

		Summary summary2 = new Summary(quantifier, List.of(summarizer1, summarizer2));
		System.out.println(summary2.doubleSummarization());

		Summary summary3 = new Summary(quantifier, List.of(summarizer1), qualifier);
		System.out.println(summary3.qualifiedSummarization());
	}
}
