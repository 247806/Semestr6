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
		this.singleSubjectSummary();
//		this.twoSubjectSummary();
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
		List<List<LinguisticTerm>> terms = new ArrayList<>();
		terms.add(temperaturesTerms);
		terms.add(timeTerms);
		terms.add(windTerms);
		terms.add(airTerms);
		terms.add(humidityTerms);
		terms.add(visibilityTerms);
		terms.add(uvTerms);
		terms.add(carbonTerms);
		terms.add(nitrogenTerms);
		terms.add(pressureTerms);

		for (Quantifier quantifier : quantifierTerms) {
			System.out.println(quantifier.getName());
			System.out.println("--------------------------------------------------");
			int amount = 0;
//			for (int i = 0; i < terms.size(); i++) {
//				for (int j = i + 1; j < terms.size(); j++) {
//					List<LinguisticTerm> list1 = terms.get(i);
//					List<LinguisticTerm> list2 = terms.get(j);
//
//					for (LinguisticTerm term1 : list1) {
//						for (LinguisticTerm term2 : list2) {
//							List<LinguisticTerm> summarizers = new ArrayList<>();
//							summarizers.add(term1);
//							summarizers.add(term2);
//
//							// Tutaj tworzysz i oceniasz podsumowanie
//							SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
//									quantifier,
//									summarizers
//							);
//							System.out.println(singleSubjectSummary.summarization());
//							if (singleSubjectSummary.getT1() > 0.01) {
//								singleSubjectSummary.print();
//								amount+=1;
//							}
//						}
//					}
//				}
//			}

//            for (int i = 0; i < terms.size(); i++) {
//				List<LinguisticTerm> qualifiers = terms.get(i);
//				List<List<LinguisticTerm>> temp = new ArrayList<>();
//				temp.addAll(terms);
//				temp.remove(i);
//				for (List<LinguisticTerm> term : temp) {
//					for (LinguisticTerm qualifier : qualifiers) {
//						for (LinguisticTerm summarizer : term) {
//
//							List<LinguisticTerm> summarizerList = new ArrayList<>();
//							summarizerList.add(summarizer);
//
//							SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
//									quantifier,
//									summarizerList,
//									qualifier
//							);
//							System.out.println(singleSubjectSummary.summarization());
////							singleSubjectSummary.summarization();
//							if (singleSubjectSummary.getT1() > 0.01) {
//								singleSubjectSummary.print();
//								amount+=1;
//							}
//						}
//					}
//				}
//            }
//			System.out.println("TEMPERATURES:");
//			for (LinguisticTerm temperatureTerm : pressureTerms) {
//				for (LinguisticTerm dateTerm : windTerms) {
//					SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(quantifier, List.of(temperatureTerm, dateTerm));
//					System.out.println(singleSubjectSummary.summarization());
//					if (singleSubjectSummary.getT1() > 0.01) {
//						singleSubjectSummary.print();
//					}
//
//				}
//				Summary summary = new Summary(quantifier, List.of(temperatureTerm));
//				System.out.println(summary.summarization());
//			}
//            int amount = 0;
            System.out.println(quantifier.getName() + " " + amount);
			System.out.println("--------------------------------------------------");
		}

		System.out.println("TEMPERATURES:");
		for (LinguisticTerm tempTerm : temperaturesTerms) {
			for (Quantifier quantifier : quantifierTerms) {
				SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(tempTerm));
				System.out.println(summary.summarization());
				if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
					summary.print();
//					amount += 1;
				}
			}
		}

		System.out.println("HUMIDITIES:");
			for (LinguisticTerm humidityTerm : humidityTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(humidityTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}

			System.out.println("TIMES:");
			for (LinguisticTerm timeTerm : timeTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(timeTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("WINDS:");
			for (LinguisticTerm windTerm : windTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(windTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("PRESSURES:");
			for (LinguisticTerm pressureTerm : pressureTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(pressureTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("VISIBILITIES:");
			for (LinguisticTerm visibilityTerm : visibilityTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(visibilityTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("UVS:");
			for (LinguisticTerm uvTerm : uvTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(uvTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("CARBONS:");
			for (LinguisticTerm carbonTerm : carbonTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(carbonTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
			}
			System.out.println("NITROGENS:");
			for (LinguisticTerm nitrogenTerm : nitrogenTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(nitrogenTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
	//                    amount+=1;
					}
				}
			}
			System.out.println("QUALITIES:");
			for (LinguisticTerm airTerm : airTerms) {
				for (Quantifier quantifier : quantifierTerms) {
					SingleSubjectSummary summary = new SingleSubjectSummary(quantifier, List.of(airTerm));
					System.out.println(summary.summarization());
					if (summary.getT1() < 0.20 && summary.getT1() > 0.01) {
						summary.print();
//						amount += 1;
					}
				}
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

		LinguisticTerm qualifier = new LinguisticTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());
		List<String> continents = List.of("America", "Europe", "Asia", "Africa");
		List<String> warmTerms = List.of("bardzo zimna", "zimna", "umiarkowana", "ciepla", "goraca");
		List<String> dayTerms = List.of("nocna", "poranna", "poludniowa", "popoludniowa", "wieczorna");
		List<String> windTerms = List.of("slaby", "umiarkowany", "silny", "bardzo silny", "gwaltowny");
		List<String> humidityTerms = List.of("suche", "umiarkowane", "wilgotne");
		List<String> pressureTerms = List.of("niskie", "normalne", "wysokie");
		List<String> visibilityTerms = List.of("slaba", "umiarkowana", "dobra", "bardzo dobra");
		List<String> uvTerms = List.of("niskie", "umiarkowane", "wysokie", "bardzo wysokie", "ekstremalne");
		List<String> carbonTerms = List.of("normalne", "wysokie", "niezdrowe", "niebezpieczne");
		List<String> nitrogenTerms = List.of("normalne", "niezdrowe", "niebezpieczne");
		List<String> airTerms = List.of("bardzo dobra", "dobra", "umiarkowana", "zla", "bardzo zla");


		for (Quantifier quantifier : quantifierTerms) {
			System.out.println("Quantifier: " + quantifier.getName());
			System.out.println("--------------------------------------------------");
			int amount = 0;
//			System.out.println("TEMPERATURA");
//			for (String temp : warmTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//						LinguisticTerm linguisticTerm1 = getLinguisticTermTemp(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermTemp(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//	                    	amount+=1;
//						}
//
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermTemp(temp, continentA);
//						linguisticTerm2 = getLinguisticTermTemp(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("PORA DNIA");
//			for (String temp : dayTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermTime(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermTime(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermTime(temp, continentA);
//						linguisticTerm2 = getLinguisticTermTime(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("WIATR");
//			for (String temp : windTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermWind(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermWind(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermWind(temp, continentA);
//						linguisticTerm2 = getLinguisticTermWind(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("CISNIENIE");
//			for (String temp : pressureTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermPressure(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermPressure(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermPressure(temp, continentA);
//						linguisticTerm2 = getLinguisticTermPressure(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("WILGOTNOSC");
//			for (String temp : humidityTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermHumidity(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermHumidity(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermHumidity(temp, continentA);
//						linguisticTerm2 = getLinguisticTermHumidity(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("WIDOCZNOSC");
//			for (String temp : visibilityTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermVisibility(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermVisibility(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermVisibility(temp, continentA);
//						linguisticTerm2 = getLinguisticTermVisibility(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("UV");
//			for (String temp : uvTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermUv(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermUv(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermUv(temp, continentA);
//						linguisticTerm2 = getLinguisticTermUv(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("CARBON");
//			for (String temp : carbonTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermCarbon(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermCarbon(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermCarbon(temp, continentA);
//						linguisticTerm2 = getLinguisticTermCarbon(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("NITROGEN");
//			for (String temp : nitrogenTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermNitrogen(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermNitrogen(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermNitrogen(temp, continentA);
//						linguisticTerm2 = getLinguisticTermNitrogen(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//
//			System.out.println("AIR");
//			for (String temp : airTerms) {
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//
//						LinguisticTerm linguisticTerm1 = getLinguisticTermAirQuality(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermAirQuality(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//						continentA = continents.get(j);
//						continentB = continents.get(i);
//						linguisticTerm1 = getLinguisticTermAirQuality(temp, continentA);
//						linguisticTerm2 = getLinguisticTermAirQuality(temp, continentB);
//						doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2
//						);
//
//						doubleSubjectSummary.firstForm();
//						if (doubleSubjectSummary.getT1() > 0.01) {
//							amount+=1;
//						}
//					}
//				}
//			}
//			System.out.println("Amount: " + amount + " for quantifier: " + quantifier.getName());
		}

//		for (Quantifier quantifier : quantifierTerms) {
//			System.out.println("Quantifier: " + quantifier.getName());
//			System.out.println("--------------------------------------------------");
//			for (String temp : airTerms) {
//				System.out.println("Nitrogen: " + temp);
//				for (int i = 0; i < continents.size(); i++) {
//					for (int j = i + 1; j < continents.size(); j++) {
//						String continentA = continents.get(i);
//						String continentB = continents.get(j);
//						qualifier.setData(measurementsRepository.findByContinent(continentB).stream()
//								.map(Measurements::getHumidity).toList());
//						LinguisticTerm linguisticTerm1 = getLinguisticTermAirQuality(temp, continentA);
//						LinguisticTerm linguisticTerm2 = getLinguisticTermAirQuality(temp, continentB);
//						DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//								quantifier,
//								continentA,
//								continentB,
//								linguisticTerm1,
//								linguisticTerm2,
//								qualifier
//						);
//
//						doubleSubjectSummary.secondForm();
//					}
//				}
//			}
//		}

		for (String temp: carbonTerms) {
			System.out.println("Temperature: " + temp);
			for (int i = 0; i < continents.size(); i++) {
				for (int j = i + 1; j < continents.size(); j++) {
					String continentA = continents.get(i);
					String continentB = continents.get(j);
					LinguisticTerm linguisticTerm1 = getLinguisticTermCarbon(temp, continentA);
					LinguisticTerm linguisticTerm2 = getLinguisticTermCarbon(temp, continentB);
					DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
							continentA,
							continentB,
							linguisticTerm1,
							linguisticTerm2
					);

					doubleSubjectSummary.fourthForm();
				}
			}

		}

//		LinguisticTerm linguisticTerm1 = getLinguisticTermCarbon("niezdrowe", "Europe");
//		LinguisticTerm linguisticTerm2 = getLinguisticTermCarbon("niezdrowe", "Asia");
//		DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//				"Asia",
//				"Europe",
//				linguisticTerm2,
//				linguisticTerm1
//		);
//
//		doubleSubjectSummary.fourthForm();

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

	public LinguisticTerm getLinguisticTermWind(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "slaby" -> linguisticTerm = new LinguisticTerm(WindValues.slaby.getName(), WindValues.slaby.getFuzzySet());
			case "umiarkowany" -> linguisticTerm = new LinguisticTerm(WindValues.umiarkowany.getName(), WindValues.umiarkowany.getFuzzySet());
			case "silny" -> linguisticTerm = new LinguisticTerm(WindValues.silny.getName(), WindValues.silny.getFuzzySet());
			case "bardzo silny" -> linguisticTerm = new LinguisticTerm(WindValues.bardzoSilny.getName(), WindValues.bardzoSilny.getFuzzySet());
			case "gwaltowny" -> linguisticTerm = new LinguisticTerm(WindValues.gwaltowny.getName(), WindValues.gwaltowny.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getWind_kph).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getWind_kph).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getWind_kph).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getWind_kph).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermHumidity(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "suche" -> linguisticTerm = new LinguisticTerm(HumidityValues.suche.getName(), HumidityValues.suche.getFuzzySet());
			case "umiarkowane" -> linguisticTerm = new LinguisticTerm(HumidityValues.umiarkowane.getName(), HumidityValues.umiarkowane.getFuzzySet());
			case "wilgotne" -> linguisticTerm = new LinguisticTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getHumidity).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getHumidity).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getHumidity).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getHumidity).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermPressure(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "niskie" -> linguisticTerm = new LinguisticTerm(PressureValues.niskie.getName(), PressureValues.niskie.getFuzzySet());
			case "normalne" -> linguisticTerm = new LinguisticTerm(PressureValues.normalne.getName(), PressureValues.normalne.getFuzzySet());
			case "wysokie" -> linguisticTerm = new LinguisticTerm(PressureValues.wysokie.getName(), PressureValues.wysokie.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getPressure_mb).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getPressure_mb).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getPressure_mb).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getPressure_mb).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermVisibility(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "slaba" -> linguisticTerm = new LinguisticTerm(VisibilityValues.slaba.getName(), VisibilityValues.slaba.getFuzzySet());
			case "umiarkowana" -> linguisticTerm = new LinguisticTerm(VisibilityValues.umiarkowana.getName(), VisibilityValues.umiarkowana.getFuzzySet());
			case "dobra" -> linguisticTerm = new LinguisticTerm(VisibilityValues.dobra.getName(), VisibilityValues.dobra.getFuzzySet());
			case "bardzo dobra" -> linguisticTerm = new LinguisticTerm(VisibilityValues.bardzoDobra.getName(), VisibilityValues.bardzoDobra.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getVisibility_km).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getVisibility_km).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getVisibility_km).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getVisibility_km).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermUv(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "niskie" -> linguisticTerm = new LinguisticTerm(UvValues.niskie.getName(), UvValues.niskie.getFuzzySet());
			case "umiarkowane" -> linguisticTerm = new LinguisticTerm(UvValues.umiarkowane.getName(), UvValues.umiarkowane.getFuzzySet());
			case "wysokie" -> linguisticTerm = new LinguisticTerm(UvValues.wysokie.getName(), UvValues.wysokie.getFuzzySet());
			case "bardzo wysokie" -> linguisticTerm = new LinguisticTerm(UvValues.bardzoWysokie.getName(), UvValues.bardzoWysokie.getFuzzySet());
			case "ekstremalne" -> linguisticTerm = new LinguisticTerm(UvValues.ekstremalne.getName(), UvValues.ekstremalne.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getUv_index).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getUv_index).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getUv_index).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getUv_index).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermCarbon(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "normalne" -> linguisticTerm = new LinguisticTerm(CoValues.normalne.getName(), CoValues.normalne.getFuzzySet());
			case "wysokie" -> linguisticTerm = new LinguisticTerm(CoValues.wysokie.getName(), CoValues.wysokie.getFuzzySet());
			case "niezdrowe" -> linguisticTerm = new LinguisticTerm(CoValues.niezdrowe.getName(), CoValues.niezdrowe.getFuzzySet());
			case "niebezpieczne" -> linguisticTerm = new LinguisticTerm(CoValues.niebezpieczne.getName(), CoValues.niebezpieczne.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getAir_quality_Carbon_Monoxide).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getAir_quality_Carbon_Monoxide).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getAir_quality_Carbon_Monoxide).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getAir_quality_Carbon_Monoxide).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermNitrogen(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "normalne" -> linguisticTerm = new LinguisticTerm(NoValues.normalne.getName(), NoValues.normalne.getFuzzySet());
			case "niezdrowe" -> linguisticTerm = new LinguisticTerm(NoValues.niezdrowe.getName(), NoValues.niezdrowe.getFuzzySet());
			case "niebezpieczne" -> linguisticTerm = new LinguisticTerm(NoValues.niebezpieczne.getName(), NoValues.niebezpieczne.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getAir_quality_Nitrogen_Dioxide).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getAir_quality_Nitrogen_Dioxide).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getAir_quality_Nitrogen_Dioxide).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getAir_quality_Nitrogen_Dioxide).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermAirQuality(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "bardzo dobra" -> linguisticTerm = new LinguisticTerm(AirValues.bardzoDobra.getName(), AirValues.bardzoDobra.getFuzzySet());
			case "dobra" -> linguisticTerm = new LinguisticTerm(AirValues.dobra.getName(), AirValues.dobra.getFuzzySet());
			case "umiarkowana" -> linguisticTerm = new LinguisticTerm(AirValues.umiarkowana.getName(), AirValues.umiarkowana.getFuzzySet());
			case "zla" -> linguisticTerm = new LinguisticTerm(AirValues.zla.getName(), AirValues.zla.getFuzzySet());
			case "bardzo zla" -> linguisticTerm = new LinguisticTerm(AirValues.bardzoZla.getName(), AirValues.bardzoZla.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		switch (continent) {
			case "Europe" -> linguisticTerm.setData(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getAir_quality_gb_defra_index).toList());
			case "Asia" -> linguisticTerm.setData(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getAir_quality_gb_defra_index).toList());
			case "Africa" -> linguisticTerm.setData(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getAir_quality_gb_defra_index).toList());
			case "America" -> linguisticTerm.setData(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getAir_quality_gb_defra_index).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}
		return linguisticTerm;
	}

	public LinguisticTerm getLinguisticTermTime(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "nocna" ->
					linguisticTerm = new LinguisticTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());
			case "poranna" ->
					linguisticTerm = new LinguisticTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());
			case "poludniowa" ->
					linguisticTerm = new LinguisticTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());
			case "popoludniowa" ->
					linguisticTerm = new LinguisticTerm(TimeValues.popoludniowa.getName(), TimeValues.popoludniowa.getFuzzySet());
			case "wieczorna" ->
					linguisticTerm = new LinguisticTerm(TimeValues.wieczorna.getName(), TimeValues.wieczorna.getFuzzySet());
			default -> throw new IllegalArgumentException("Unknown term: " + term);
		}

		List<LocalDateTime> dates = new ArrayList<>();
		List<Double> hours = new ArrayList<>();

		switch (continent) {
			case "Europe" -> dates.addAll(measurementsRepository.findByContinent("Europe").stream()
					.map(Measurements::getLast_updated).toList());
			case "Asia" -> dates.addAll(measurementsRepository.findByContinent("Asia").stream()
					.map(Measurements::getLast_updated).toList());
			case "Africa" -> dates.addAll(measurementsRepository.findByContinent("Africa").stream()
					.map(Measurements::getLast_updated).toList());
			case "America" -> dates.addAll(measurementsRepository.findByContinent("America").stream()
					.map(Measurements::getLast_updated).toList());
			default -> throw new IllegalArgumentException("Unknown continent: " + continent);
		}

		for (LocalDateTime date : dates) {
			hours.add((double) date.getHour() + date.getMinute() / 60.0);
		}
		linguisticTerm.setData(hours);
		return linguisticTerm;
	}
}

