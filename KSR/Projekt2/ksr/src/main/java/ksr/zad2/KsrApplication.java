package ksr.zad2;

import ksr.zad2.fuzzy.*;
import ksr.zad2.fuzzy.GaussianFunction;
import ksr.zad2.fuzzy.TrapezoidalFunction;
import ksr.zad2.fuzzy.TriangularFunction;
import ksr.zad2.model.Measurements;
import ksr.zad2.repository.MeasurementsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		LinguisticVariable linguisticVariable = new LinguisticVariable("Temperatura",-25.0, 50.0);

		linguisticVariable.addTerm(terminy1.getFirst(), new TrapezoidalFunction(-26, -25, -15, -5));
		linguisticVariable.addTerm(terminy1.get(1), new TrapezoidalFunction(-10, 0, 5, 10));
		linguisticVariable.addTerm(terminy1.get(2), new TrapezoidalFunction(5, 10, 15, 20));
		linguisticVariable.addTerm(terminy1.get(3), new TrapezoidalFunction(15, 20, 25, 30));
		linguisticVariable.addTerm(terminy1.get(4), new TrapezoidalFunction(25, 35, 50, 51));

		LinguisticVariable linguisticVariable3 = new LinguisticVariable("Przynależność",0.0, 1.0);

		linguisticVariable3.addTerm(quantifiers.getFirst(), new GaussianFunction(0.0, 0.06));
		linguisticVariable3.addTerm(quantifiers.get(1), new GaussianFunction(0.3, 0.06));
		linguisticVariable3.addTerm(quantifiers.get(2), new GaussianFunction(0.5, 0.06));
		linguisticVariable3.addTerm(quantifiers.get(3), new GaussianFunction(0.7, 0.06));
		linguisticVariable3.addTerm(quantifiers.get(4), new GaussianFunction(1.0, 0.06));

		LinguisticVariable linguisticVariable2 = new LinguisticVariable("Wilgotność", 0.0, 100.0);
		linguisticVariable2.addTerm(terminy2.getFirst(), new TriangularFunction(0.0,  0.0, 0.5));
		linguisticVariable2.addTerm(terminy2.get(1), new TriangularFunction(0.30,  0.6, 0.8));
		linguisticVariable2.addTerm(terminy2.get(2), new TriangularFunction(0.6,  1.0, 1.01));

		LinguisticVariable linguisticVariable4 = new LinguisticVariable("Pora dnia", 0.0, 24.0);
		linguisticVariable4.addTerm(terminy3.getFirst(), new TrapezoidalFunction(0,  5, 8, 21));
		linguisticVariable4.addTerm(terminy3.get(1), new TrapezoidalFunction(5,  7, 10, 12));
		linguisticVariable4.addTerm(terminy3.get(2), new TrapezoidalFunction(10, 11, 13, 15));
		linguisticVariable4.addTerm(terminy3.get(3), new TrapezoidalFunction(15, 16, 18, 20));
		linguisticVariable4.addTerm(terminy3.get(4), new TrapezoidalFunction(19, 20, 22, 24));

		Quantifier quantifier = new Quantifier("trochę", linguisticVariable3, true);
		Summarizer summarizer1 = new Summarizer("gorąco", linguisticVariable, temperatures);

		Summarizer summarizer2 = new Summarizer(terminy2.get(2), linguisticVariable2, humidities);
		Qualifier qualifier = new Qualifier(terminy3.get(4), linguisticVariable4, hours);

		Summary summary = new Summary(quantifier, List.of(summarizer1));
		System.out.println(summary.summarization());

		Summary summary2 = new Summary(quantifier, List.of(summarizer1, summarizer2));
		System.out.println(summary2.summarization());

		Summary summary3 = new Summary(quantifier, List.of(summarizer1), qualifier);
		System.out.println(summary3.qualifiedSummarization());
	}
}
