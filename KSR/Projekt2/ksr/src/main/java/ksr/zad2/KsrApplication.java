package ksr.zad2;

import ksr.zad2.fuzzy.*;
import ksr.zad2.fuzzy.GaussianFunction;
import ksr.zad2.fuzzy.TrapezoidalFunction;
import ksr.zad2.fuzzy.TriangularFunction;
import ksr.zad2.model.Measurements;
import ksr.zad2.model.variables.HumidityValues;
import ksr.zad2.model.variables.QuantifierValues;
import ksr.zad2.model.variables.TempValues;
import ksr.zad2.model.variables.TimeValues;
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
		List<Double> humidities = new ArrayList<>();
		List<LocalDateTime> dates = new ArrayList<>();
		List<Double> pressure = new ArrayList<>();
		List<Double> wind = new ArrayList<>();
		List<Double> visibility = new ArrayList<>();
		List<Double> uv = new ArrayList<>();
		List<Double> carbon = new ArrayList<>();
		List<Double> nitrogen = new ArrayList<>();
		List<Double> quality = new ArrayList<>();
		for (Measurements measurement : allMeasurements) {
			temperatures.add((double) measurement.getTemperature_celsius());
			humidities.add((double) measurement.getHumidity());
			dates.add(measurement.getLast_updated());
			pressure.add((double) measurement.getPressure_mb());
			wind.add((double) measurement.getWind_kph());
			visibility.add((double) measurement.getVisibility_km());
			uv.add((double) measurement.getUv_index());
			carbon.add((double) measurement.getAir_quality_Carbon_Monoxide());
			nitrogen.add((double) measurement.getAir_quality_Nitrogen_Dioxide());
			quality.add((double) measurement.getAir_quality_gb_defra_index());
		}

		List<Double> hours = new ArrayList<>();
		for (LocalDateTime date : dates) {
			hours.add((double) date.getHour() + date.getMinute() / 60.0);
		}

		LinguisticVariable linguisticVariable = TempValues.tempVariable;

		linguisticVariable.addTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
		linguisticVariable.addTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
		linguisticVariable.addTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
		linguisticVariable.addTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
		linguisticVariable.addTerm(TempValues.goraca.getName(), TempValues.goraca.getFuzzySet());

		LinguisticVariable linguisticVariable3 = QuantifierValues.linguisticVariableQ;

		linguisticVariable3.addTerm(QuantifierValues.Q1.getName(), QuantifierValues.Q1.getFuzzySet());
		linguisticVariable3.addTerm(QuantifierValues.Q2.getName(), QuantifierValues.Q2.getFuzzySet());
		linguisticVariable3.addTerm(QuantifierValues.Q3.getName(), QuantifierValues.Q3.getFuzzySet());
		linguisticVariable3.addTerm(QuantifierValues.Q4.getName(), QuantifierValues.Q4.getFuzzySet());
		linguisticVariable3.addTerm(QuantifierValues.Q5.getName(), QuantifierValues.Q5.getFuzzySet());

		LinguisticVariable linguisticVariable2 = HumidityValues.humidityVariable;
		linguisticVariable2.addTerm(HumidityValues.suche.getName(), HumidityValues.suche.getFuzzySet());
		linguisticVariable2.addTerm(HumidityValues.umiarkowane.getName(), HumidityValues.umiarkowane.getFuzzySet());
		linguisticVariable2.addTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());

		LinguisticVariable linguisticVariable4 = TimeValues.timeVariable;
		linguisticVariable4.addTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());
		linguisticVariable4.addTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());
		linguisticVariable4.addTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());
		linguisticVariable4.addTerm(TimeValues.popoludniowa.getName(), TimeValues.popoludniowa.getFuzzySet());
		linguisticVariable4.addTerm(TimeValues.wieczorna.getName(), TimeValues.wieczorna.getFuzzySet());

		Quantifier quantifier = QuantifierValues.Q2;
		LinguisticTerm summarizer1 = TempValues.goraca;
		summarizer1.setData(temperatures);

		LinguisticTerm summarizer2 =HumidityValues.wilgotne;
		summarizer2.setData(humidities);
		LinguisticTerm qualifier = TimeValues.wieczorna;
		qualifier.setData(hours);

		Summary summary = new Summary(quantifier, List.of(summarizer1));
		System.out.println(summary.summarization());

		Summary summary2 = new Summary(quantifier, List.of(summarizer1, summarizer2));
		System.out.println(summary2.summarization());

		Summary summary3 = new Summary(quantifier, List.of(summarizer1), qualifier);
		System.out.println(summary3.qualifiedSummarization());
	}
}
