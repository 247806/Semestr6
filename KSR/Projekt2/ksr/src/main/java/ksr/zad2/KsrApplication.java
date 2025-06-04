package ksr.zad2;

import ksr.zad2.fuzzy.*;
import ksr.zad2.model.Measurements;
import ksr.zad2.model.variables.*;
import ksr.zad2.repository.MeasurementsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
//		this.singleSubjectSummary();
		this.twoSubjectSummary();
	}


	public void singleSubjectSummary() {
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

		// Temperatura
		LinguisticVariable tempVariable = TempValues.tempVariable;
		tempVariable.addTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
		tempVariable.addTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
		tempVariable.addTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
		tempVariable.addTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
		tempVariable.addTerm(TempValues.goraca.getName(), TempValues.goraca.getFuzzySet());
		List<LinguisticTerm> temperaturesTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : tempVariable.getTerms().entrySet()) {
			// Create LinguisticTerm for each temperature term
			// Set the data for the temperature term
			LinguisticTerm temperatureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			temperatureTerm.setData(temperatures);
			temperaturesTerms.add(temperatureTerm);
		}


		// Kwalifikatory
		LinguisticVariable quatifiers = QuantifierValues.linguisticVariableQ;
		quatifiers.addTerm(QuantifierValues.Q1.getName(), QuantifierValues.Q1.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q2.getName(), QuantifierValues.Q2.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q3.getName(), QuantifierValues.Q3.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q4.getName(), QuantifierValues.Q4.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q5.getName(), QuantifierValues.Q5.getFuzzySet());
		List<Quantifier> quantifierTerms = new ArrayList<>();
		quantifierTerms.add(QuantifierValues.Q1);
		quantifierTerms.add(QuantifierValues.Q2);
		quantifierTerms.add(QuantifierValues.Q3);
		quantifierTerms.add(QuantifierValues.Q4);
		quantifierTerms.add(QuantifierValues.Q5);

		// Wilgotność
		LinguisticVariable humidity = HumidityValues.humidityVariable;
		humidity.addTerm(HumidityValues.suche.getName(), HumidityValues.suche.getFuzzySet());
		humidity.addTerm(HumidityValues.umiarkowane.getName(), HumidityValues.umiarkowane.getFuzzySet());
		humidity.addTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());
		List<LinguisticTerm> humidityTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : humidity.getTerms().entrySet()) {
			LinguisticTerm humidityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			humidityTerm.setData(humidities);
			humidityTerms.add(humidityTerm);
		}

		// Czas
		LinguisticVariable timeVariable = TimeValues.timeVariable;
		timeVariable.addTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());
		timeVariable.addTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());
		timeVariable.addTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());
		timeVariable.addTerm(TimeValues.popoludniowa.getName(), TimeValues.popoludniowa.getFuzzySet());
		timeVariable.addTerm(TimeValues.wieczorna.getName(), TimeValues.wieczorna.getFuzzySet());
		List<LinguisticTerm> timeTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : timeVariable.getTerms().entrySet()) {
			LinguisticTerm timeTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			timeTerm.setData(hours);
			timeTerms.add(timeTerm);
		}


		// Wiatr
		LinguisticVariable windVariable = WindValues.windVariable;
		windVariable.addTerm(WindValues.slaby.getName(), WindValues.slaby.getFuzzySet());
		windVariable.addTerm(WindValues.umiarkowany.getName(), WindValues.umiarkowany.getFuzzySet());
		windVariable.addTerm(WindValues.silny.getName(), WindValues.silny.getFuzzySet());
		windVariable.addTerm(WindValues.bardzoSilny.getName(), WindValues.bardzoSilny.getFuzzySet());
		windVariable.addTerm(WindValues.gwaltowny.getName(), WindValues.gwaltowny.getFuzzySet());
		List<LinguisticTerm> windTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : windVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each wind term
			LinguisticTerm windTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			// Set the data for the wind term
			windTerm.setData(wind);
			windTerms.add(windTerm);
		}


		// Cisnienie
		LinguisticVariable pressureVariable = PressureValues.pressureVariable;
		pressureVariable.addTerm(PressureValues.niskie.getName(), PressureValues.niskie.getFuzzySet());
		pressureVariable.addTerm(PressureValues.normalne.getName(), PressureValues.normalne.getFuzzySet());
		pressureVariable.addTerm(PressureValues.wysokie.getName(), PressureValues.wysokie.getFuzzySet());
		List<LinguisticTerm> pressureTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : pressureVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each pressure term
			// Set the data for the pressure term
			LinguisticTerm pressureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			pressureTerm.setData(pressure);
			pressureTerms.add(pressureTerm);
		}


		// Widoczność
		LinguisticVariable visibilityVariable = VisibilityValues.visibilityVariable;
		visibilityVariable.addTerm(VisibilityValues.slaba.getName(), VisibilityValues.slaba.getFuzzySet());
		visibilityVariable.addTerm(VisibilityValues.umiarkowana.getName(), VisibilityValues.umiarkowana.getFuzzySet());
		visibilityVariable.addTerm(VisibilityValues.dobra.getName(), VisibilityValues.dobra.getFuzzySet());
		visibilityVariable.addTerm(VisibilityValues.bardzoDobra.getName(), VisibilityValues.bardzoDobra.getFuzzySet());
		List<LinguisticTerm> visibilityTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : visibilityVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each visibility term
			LinguisticTerm visibilityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			// Set the data for the visibility term
			visibilityTerm.setData(visibility);
			visibilityTerms.add(visibilityTerm);
		}


		// Indeks UV
		LinguisticVariable uvVariable = UvValues.uvIndexVariable;
		uvVariable.addTerm(UvValues.niskie.getName(), UvValues.niskie.getFuzzySet());
		uvVariable.addTerm(UvValues.umiarkowane.getName(), UvValues.umiarkowane.getFuzzySet());
		uvVariable.addTerm(UvValues.wysokie.getName(), UvValues.wysokie.getFuzzySet());
		uvVariable.addTerm(UvValues.bardzoWysokie.getName(), UvValues.bardzoWysokie.getFuzzySet());
		uvVariable.addTerm(UvValues.ekstremalne.getName(), UvValues.ekstremalne.getFuzzySet());
		List<LinguisticTerm> uvTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : uvVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each UV term
			// Set the data for the UV term
			LinguisticTerm uvTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			uvTerm.setData(uv);
			uvTerms.add(uvTerm);
		}


		// Jakość powietrza CO2
		LinguisticVariable carbonVariable = CoValues.coQualityVariable;
		carbonVariable.addTerm(CoValues.normalne.getName(), CoValues.normalne.getFuzzySet());
		carbonVariable.addTerm(CoValues.wysokie.getName(), CoValues.wysokie.getFuzzySet());
		carbonVariable.addTerm(CoValues.niezdrowe.getName(), CoValues.niezdrowe.getFuzzySet());
		carbonVariable.addTerm(CoValues.niebezpieczne.getName(), CoValues.niebezpieczne.getFuzzySet());
		List<LinguisticTerm> carbonTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : carbonVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each carbon term
			// Set the data for the carbon term
			LinguisticTerm carbonTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			carbonTerm.setData(carbon);
			carbonTerms.add(carbonTerm);
		}


		// Jakość powietrza NO2
		LinguisticVariable nitrogenVariable = NoValues.no2QualityVariable;
		nitrogenVariable.addTerm(NoValues.normalne.getName(), NoValues.normalne.getFuzzySet());
		nitrogenVariable.addTerm(NoValues.niezdrowe.getName(), NoValues.niezdrowe.getFuzzySet());
		nitrogenVariable.addTerm(NoValues.niebezpieczne.getName(), NoValues.niebezpieczne.getFuzzySet());
		List<LinguisticTerm> nitrogenTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : nitrogenVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each nitrogen term
			// Set the data for the nitrogen term
			LinguisticTerm nitrogenTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			nitrogenTerm.setData(nitrogen);
			nitrogenTerms.add(nitrogenTerm);
		}


		// Jakość powietrza
		LinguisticVariable qualityVariable = AirValues.airQuality;
		qualityVariable.addTerm(AirValues.bardzoDobra.getName(), AirValues.bardzoDobra.getFuzzySet());
		qualityVariable.addTerm(AirValues.dobra.getName(), AirValues.dobra.getFuzzySet());
		qualityVariable.addTerm(AirValues.umiarkowana.getName(), AirValues.umiarkowana.getFuzzySet());
		qualityVariable.addTerm(AirValues.zla.getName(), AirValues.zla.getFuzzySet());
		qualityVariable.addTerm(AirValues.bardzoZla.getName(), AirValues.bardzoZla.getFuzzySet());
		List<LinguisticTerm> airTerms = new ArrayList<>();
		for (Map.Entry<String, FuzzySet> entry : qualityVariable.getTerms().entrySet()) {
			// Create a LinguisticTerm for each air quality term
			// Set the data for the air quality term
			LinguisticTerm airTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
			airTerm.setData(quality);
			airTerms.add(airTerm);
		}

		List<Measurements> measurements = measurementsRepository.findByContinent("Europe");
//		for (int i = 0; i < 4; i++) {
//			System.out.println(measurements.get(i).getTimezone());
//		}
		System.out.println("Measurements count: " + measurements.size());

		for (Quantifier quantifier : quantifierTerms) {
			System.out.println(quantifier.getName());
			System.out.println("--------------------------------------------------");
//			System.out.println("TEMPERATURES:");
//			for (LinguisticTerm temperatureTerm : carbonTerms) {
//				for (LinguisticTerm dateTerm : visibilityTerms) {
//					SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(quantifier, List.of(temperatureTerm), dateTerm);
//					System.out.println(singleSubjectSummary.summarization());
//					if (singleSubjectSummary.getT1() > 0.01) {
//						singleSubjectSummary.print();
//					}
//
//				}
//				Summary summary = new Summary(quantifier, List.of(temperatureTerm));
//				System.out.println(summary.summarization());
//			}

//			System.out.println("HUMIDITIES:");
//			for (LinguisticTerm humidityTerm : humidityTerms) {
//				Summary summary = new Summary(quantifier, List.of(humidityTerm));
//				System.out.println(summary.summarization());
//			}

//			System.out.println("TIMES:");
//			for (LinguisticTerm timeTerm : timeTerms) {
//				Summary summary = new Summary(quantifier, List.of(timeTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("WINDS:");
//			for (LinguisticTerm windTerm : windTerms) {
//				Summary summary = new Summary(quantifier, List.of(windTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("PRESSURES:");
//			for (LinguisticTerm pressureTerm : pressureTerms) {
//				Summary summary = new Summary(quantifier, List.of(pressureTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("VISIBILITIES:");
//			for (LinguisticTerm visibilityTerm : visibilityTerms) {
//				Summary summary = new Summary(quantifier, List.of(visibilityTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("UVS:");
//			for (LinguisticTerm uvTerm : uvTerms) {
//				Summary summary = new Summary(quantifier, List.of(uvTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("CARBONS:");
//			for (LinguisticTerm carbonTerm : carbonTerms) {
//				Summary summary = new Summary(quantifier, List.of(carbonTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("NITROGENS:");
//			for (LinguisticTerm nitrogenTerm : nitrogenTerms) {
//				Summary summary = new Summary(quantifier, List.of(nitrogenTerm));
//				System.out.println(summary.summarization());
//			}
//			System.out.println("QUALITIES:");
//			for (LinguisticTerm airTerm : airTerms) {
//				Summary summary = new Summary(quantifier, List.of(airTerm));
//				System.out.println(summary.summarization());
//			}
			System.out.println("--------------------------------------------------");
		}
	}

	public void twoSubjectSummary() {
		// Kwalifikatory
		LinguisticVariable quatifiers = QuantifierValues.linguisticVariableQ;
		quatifiers.addTerm(QuantifierValues.Q1.getName(), QuantifierValues.Q1.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q2.getName(), QuantifierValues.Q2.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q3.getName(), QuantifierValues.Q3.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q4.getName(), QuantifierValues.Q4.getFuzzySet());
		quatifiers.addTerm(QuantifierValues.Q5.getName(), QuantifierValues.Q5.getFuzzySet());
		List<Quantifier> quantifierTerms = new ArrayList<>();
		quantifierTerms.add(QuantifierValues.Q1);
		quantifierTerms.add(QuantifierValues.Q2);
		quantifierTerms.add(QuantifierValues.Q3);
		quantifierTerms.add(QuantifierValues.Q4);
		quantifierTerms.add(QuantifierValues.Q5);


		List<Measurements> europeMeasurements = measurementsRepository.findByContinent("Europe");
		List<Measurements> asiaMeasurements = measurementsRepository.findByContinent("Asia");
		List<Measurements> africaMeasurements = measurementsRepository.findByContinent("Africa");
		List<Measurements> americaMeasurements = measurementsRepository.findByContinent("America");

		System.out.println("Europe measurements count: " + europeMeasurements.size());
		System.out.println("Asia measurements count: " + asiaMeasurements.size());
		System.out.println("Africa measurements count: " + africaMeasurements.size());
		System.out.println("America measurements count: " + americaMeasurements.size());


		List<String> continents = List.of("Asia", "Africa", "America");
		List<String> warmTerms = List.of("bardzo zimna", "zimna", "umiarkowana", "ciepla", "goraca");

		for (Quantifier quantifier : quantifierTerms) {
			System.out.println("Quantifier: " + quantifier.getName());
			System.out.println("--------------------------------------------------");
			for (String temp: warmTerms) {
				System.out.println("Temperature: " + temp);
				for (String continent : continents) {
					System.out.println("Continent: " + continent);
					LinguisticTerm linguisticTerm1 = getLinguisticTermTemp(temp, "Europe");
					LinguisticTerm linguisticTerm2 = getLinguisticTermTemp(temp, continent);

					DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
							quantifier,
							"Europe",
							"Asia",
							linguisticTerm1,
							linguisticTerm2
					);

					doubleSubjectSummary.firstForm();
				}
			}
		}

	}

	public LinguisticTerm getLinguisticTermTemp(String term, String continent) {
        LinguisticTerm linguisticTerm;

		switch (term) {
            case "bardzo zimna" -> linguisticTerm = new LinguisticTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
            case "zimna" -> linguisticTerm = new LinguisticTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
            case "umiarkowana" -> linguisticTerm = new LinguisticTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
            case "ciepla" -> linguisticTerm = new LinguisticTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
            case "goraca" -> linguisticTerm = new LinguisticTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
            default -> throw new IllegalArgumentException("Unknown term: " + term);
        }

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getTemperature_celsius).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getTemperature_celsius).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getTemperature_celsius).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getTemperature_celsius).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}
}

