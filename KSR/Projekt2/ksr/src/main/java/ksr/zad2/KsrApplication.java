package ksr.zad2;

import ksr.zad2.fuzzy.*;
import ksr.zad2.model.Measurements;
import ksr.zad2.model.variables.*;
import ksr.zad2.repository.MeasurementsRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class KsrApplication implements CommandLineRunner {

	public static MeasurementsRepository measurementsRepository;

	public KsrApplication(MeasurementsRepository measurementsRepository) {
		this.measurementsRepository = measurementsRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(KsrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//this.singleSubjectSummary();
//		this.twoSubjectSummary();
	}


	public static void singleSubjectSummary(String data1, String data2, String data3, String data4, String data5, String sumariser1, String sumariser2, String sumariser3, String sumariser4, String qualificator, String quantificator, List<Double> weight) {
		List<LinguisticTerm> sumariser = new ArrayList<>();
		List<LinguisticTerm> qualifier = new ArrayList<>();

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

		// Kwalifikatory
		LinguisticVariable quatifiers = QuantifierValues.linguisticVariableQ;
		List<Quantifier> quantifierTerms = new ArrayList<>();
		if (quantificator.equals(QuantifierValues.Q1.getName())) {
			quatifiers.addTerm(QuantifierValues.Q1.getName(), QuantifierValues.Q1.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q1);
		} else if (quantificator.equals(QuantifierValues.Q2.getName())) {
			quatifiers.addTerm(QuantifierValues.Q2.getName(), QuantifierValues.Q2.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q2);
		} else if (quantificator.equals(QuantifierValues.Q3.getName())) {
			quatifiers.addTerm(QuantifierValues.Q3.getName(), QuantifierValues.Q3.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q3);
		} else if (quantificator.equals(QuantifierValues.Q4.getName())) {
			quatifiers.addTerm(QuantifierValues.Q4.getName(), QuantifierValues.Q4.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q4);
		} else if (quantificator.equals(QuantifierValues.Q5.getName())) {
			quatifiers.addTerm(QuantifierValues.Q5.getName(), QuantifierValues.Q5.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q5);
		}

		// Temperatura
		if (data1.equals(TempValues.tempVariable.getName()) || data2.equals(TempValues.tempVariable.getName()) || data3.equals(TempValues.tempVariable.getName()) || data4.equals(TempValues.tempVariable.getName())) {
			LinguisticVariable tempVariable = TempValues.tempVariable;
			System.out.println(tempVariable);
			if (sumariser1.equals(TempValues.bardzoZimna.getName()) || sumariser2.equals(TempValues.bardzoZimna.getName()) || sumariser3.equals(TempValues.bardzoZimna.getName()) || sumariser4.equals(TempValues.bardzoZimna.getName())) {
				tempVariable.addTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
			} else if (sumariser1.equals(TempValues.zimna.getName()) || sumariser2.equals(TempValues.zimna.getName()) || sumariser3.equals(TempValues.zimna.getName()) || sumariser4.equals(TempValues.zimna.getName())) {
				tempVariable.addTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
			} else if (sumariser1.equals(TempValues.umiarkowana.getName()) || sumariser2.equals(TempValues.umiarkowana.getName()) || sumariser3.equals(TempValues.umiarkowana.getName()) || sumariser4.equals(TempValues.umiarkowana.getName())) {
				tempVariable.addTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
			} else if (sumariser1.equals(TempValues.ciepla.getName()) || sumariser2.equals(TempValues.ciepla.getName()) || sumariser3.equals(TempValues.ciepla.getName()) || sumariser4.equals(TempValues.ciepla.getName())) {
				tempVariable.addTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
			} else if (sumariser1.equals(TempValues.goraca.getName()) || sumariser2.equals(TempValues.goraca.getName()) || sumariser3.equals(TempValues.goraca.getName()) || sumariser4.equals(TempValues.goraca.getName())) {
				tempVariable.addTerm(TempValues.goraca.getName(), TempValues.goraca.getFuzzySet());
			}
			//List<LinguisticTerm> temperaturesTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : tempVariable.getTerms().entrySet()) {
				// Create LinguisticTerm for each temperature term
				// Set the data for the temperature term
				LinguisticTerm temperatureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				temperatureTerm.setData(temperatures);
				sumariser.add(temperatureTerm);
			}
			tempVariable.clearTerms();
		}


		// Wilgotność
		if (data1.equals(HumidityValues.humidityVariable.getName()) || data2.equals(HumidityValues.humidityVariable.getName()) || data3.equals(HumidityValues.humidityVariable.getName()) || data4.equals(HumidityValues.humidityVariable.getName())) {
			LinguisticVariable humidity = HumidityValues.humidityVariable;
			if (sumariser1.equals(HumidityValues.suche.getName()) || sumariser2.equals(HumidityValues.suche.getName()) || sumariser3.equals(HumidityValues.suche.getName()) || sumariser4.equals(HumidityValues.suche.getName())) {
				humidity.addTerm(HumidityValues.suche.getName(), HumidityValues.suche.getFuzzySet());
			} else if (sumariser1.equals(HumidityValues.umiarkowane.getName()) || sumariser2.equals(HumidityValues.umiarkowane.getName()) || sumariser3.equals(HumidityValues.umiarkowane.getName()) || sumariser4.equals(HumidityValues.umiarkowane.getName())) {
				humidity.addTerm(HumidityValues.umiarkowane.getName(), HumidityValues.umiarkowane.getFuzzySet());
			} else if (sumariser1.equals(HumidityValues.wilgotne.getName()) || sumariser2.equals(HumidityValues.wilgotne.getName()) || sumariser3.equals(HumidityValues.wilgotne.getName()) || sumariser4.equals(HumidityValues.wilgotne.getName())) {
				humidity.addTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());
			}
//			List<LinguisticTerm> humidityTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : humidity.getTerms().entrySet()) {
				LinguisticTerm humidityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				humidityTerm.setData(humidities);
				sumariser.add(humidityTerm);
			}
			humidity.clearTerms();
		}


		if (data1.equals(TimeValues.timeVariable.getName()) ||
				data2.equals(TimeValues.timeVariable.getName()) ||
				data3.equals(TimeValues.timeVariable.getName()) ||
				data4.equals(TimeValues.timeVariable.getName())) {

			LinguisticVariable timeVariable = TimeValues.timeVariable;

			if (sumariser1.equals(TimeValues.nocna.getName()) ||
					sumariser2.equals(TimeValues.nocna.getName()) ||
					sumariser3.equals(TimeValues.nocna.getName()) ||
					sumariser4.equals(TimeValues.nocna.getName())) {

				timeVariable.addTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());

			} else if (sumariser1.equals(TimeValues.poranna.getName()) ||
					sumariser2.equals(TimeValues.poranna.getName()) ||
					sumariser3.equals(TimeValues.poranna.getName()) ||
					sumariser4.equals(TimeValues.poranna.getName())) {

				timeVariable.addTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());

			} else if (sumariser1.equals(TimeValues.poludniowa.getName()) ||
					sumariser2.equals(TimeValues.poludniowa.getName()) ||
					sumariser3.equals(TimeValues.poludniowa.getName()) ||
					sumariser4.equals(TimeValues.poludniowa.getName())) {

				timeVariable.addTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());

			} else if (sumariser1.equals(TimeValues.popoludniowa.getName()) ||
					sumariser2.equals(TimeValues.popoludniowa.getName()) ||
					sumariser3.equals(TimeValues.popoludniowa.getName()) ||
					sumariser4.equals(TimeValues.popoludniowa.getName())) {

				timeVariable.addTerm(TimeValues.popoludniowa.getName(), TimeValues.popoludniowa.getFuzzySet());

			} else if (sumariser1.equals(TimeValues.wieczorna.getName()) ||
					sumariser2.equals(TimeValues.wieczorna.getName()) ||
					sumariser3.equals(TimeValues.wieczorna.getName()) ||
					sumariser4.equals(TimeValues.wieczorna.getName())) {

				timeVariable.addTerm(TimeValues.wieczorna.getName(), TimeValues.wieczorna.getFuzzySet());
			}

			//List<LinguisticTerm> timeTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : timeVariable.getTerms().entrySet()) {
				LinguisticTerm timeTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				timeTerm.setData(hours);
				sumariser.add(timeTerm);
			}

			timeVariable.clearTerms(); // opcjonalne: wyczyść po wykorzystaniu
		}

		// Wiatr
		if (data1.equals(WindValues.windVariable.getName()) ||
				data2.equals(WindValues.windVariable.getName()) ||
				data3.equals(WindValues.windVariable.getName()) ||
				data4.equals(WindValues.windVariable.getName())) {

			LinguisticVariable windVariable = WindValues.windVariable;
			//windVariable.clearTerms(); // usuń wcześniejsze termy

			if (sumariser1.equals(WindValues.slaby.getName()) ||
					sumariser2.equals(WindValues.slaby.getName()) ||
					sumariser3.equals(WindValues.slaby.getName()) ||
					sumariser4.equals(WindValues.slaby.getName())) {

				windVariable.addTerm(WindValues.slaby.getName(), WindValues.slaby.getFuzzySet());

			} else if (sumariser1.equals(WindValues.umiarkowany.getName()) ||
					sumariser2.equals(WindValues.umiarkowany.getName()) ||
					sumariser3.equals(WindValues.umiarkowany.getName()) ||
					sumariser4.equals(WindValues.umiarkowany.getName())) {

				windVariable.addTerm(WindValues.umiarkowany.getName(), WindValues.umiarkowany.getFuzzySet());

			} else if (sumariser1.equals(WindValues.silny.getName()) ||
					sumariser2.equals(WindValues.silny.getName()) ||
					sumariser3.equals(WindValues.silny.getName()) ||
					sumariser4.equals(WindValues.silny.getName())) {

				windVariable.addTerm(WindValues.silny.getName(), WindValues.silny.getFuzzySet());

			} else if (sumariser1.equals(WindValues.bardzoSilny.getName()) ||
					sumariser2.equals(WindValues.bardzoSilny.getName()) ||
					sumariser3.equals(WindValues.bardzoSilny.getName()) ||
					sumariser4.equals(WindValues.bardzoSilny.getName())) {

				windVariable.addTerm(WindValues.bardzoSilny.getName(), WindValues.bardzoSilny.getFuzzySet());

			} else if (sumariser1.equals(WindValues.gwaltowny.getName()) ||
					sumariser2.equals(WindValues.gwaltowny.getName()) ||
					sumariser3.equals(WindValues.gwaltowny.getName()) ||
					sumariser4.equals(WindValues.gwaltowny.getName())) {

				windVariable.addTerm(WindValues.gwaltowny.getName(), WindValues.gwaltowny.getFuzzySet());
			}

			//List<LinguisticTerm> windTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : windVariable.getTerms().entrySet()) {
				LinguisticTerm windTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				windTerm.setData(wind);
				sumariser.add(windTerm);
			}

			windVariable.clearTerms(); // opcjonalne – jeśli chcesz wyczyścić po użyciu
		}


		// Cisnienie
		if (data1.equals(PressureValues.pressureVariable.getName()) ||
				data2.equals(PressureValues.pressureVariable.getName()) ||
				data3.equals(PressureValues.pressureVariable.getName()) ||
				data4.equals(PressureValues.pressureVariable.getName())) {

			LinguisticVariable pressureVariable = PressureValues.pressureVariable;

			if (sumariser1.equals(PressureValues.niskie.getName()) ||
					sumariser2.equals(PressureValues.niskie.getName()) ||
					sumariser3.equals(PressureValues.niskie.getName()) ||
					sumariser4.equals(PressureValues.niskie.getName())) {

				pressureVariable.addTerm(PressureValues.niskie.getName(), PressureValues.niskie.getFuzzySet());

			} else if (sumariser1.equals(PressureValues.normalne.getName()) ||
					sumariser2.equals(PressureValues.normalne.getName()) ||
					sumariser3.equals(PressureValues.normalne.getName()) ||
					sumariser4.equals(PressureValues.normalne.getName())) {

				pressureVariable.addTerm(PressureValues.normalne.getName(), PressureValues.normalne.getFuzzySet());

			} else if (sumariser1.equals(PressureValues.wysokie.getName()) ||
					sumariser2.equals(PressureValues.wysokie.getName()) ||
					sumariser3.equals(PressureValues.wysokie.getName()) ||
					sumariser4.equals(PressureValues.wysokie.getName())) {

				pressureVariable.addTerm(PressureValues.wysokie.getName(), PressureValues.wysokie.getFuzzySet());
			}

			//List<LinguisticTerm> pressureTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : pressureVariable.getTerms().entrySet()) {
				LinguisticTerm pressureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				pressureTerm.setData(pressure);
				sumariser.add(pressureTerm);
			}

			pressureVariable.clearTerms(); // Opcjonalnie – jeśli termy mają być jednorazowo używane
		}


		// Widoczność
		if (data1.equals(VisibilityValues.visibilityVariable.getName()) ||
				data2.equals(VisibilityValues.visibilityVariable.getName()) ||
				data3.equals(VisibilityValues.visibilityVariable.getName()) ||
				data4.equals(VisibilityValues.visibilityVariable.getName())) {

			LinguisticVariable visibilityVariable = VisibilityValues.visibilityVariable;

			if (sumariser1.equals(VisibilityValues.slaba.getName()) ||
					sumariser2.equals(VisibilityValues.slaba.getName()) ||
					sumariser3.equals(VisibilityValues.slaba.getName()) ||
					sumariser4.equals(VisibilityValues.slaba.getName())) {

				visibilityVariable.addTerm(VisibilityValues.slaba.getName(), VisibilityValues.slaba.getFuzzySet());

			} else if (sumariser1.equals(VisibilityValues.umiarkowana.getName()) ||
					sumariser2.equals(VisibilityValues.umiarkowana.getName()) ||
					sumariser3.equals(VisibilityValues.umiarkowana.getName()) ||
					sumariser4.equals(VisibilityValues.umiarkowana.getName())) {

				visibilityVariable.addTerm(VisibilityValues.umiarkowana.getName(), VisibilityValues.umiarkowana.getFuzzySet());

			} else if (sumariser1.equals(VisibilityValues.dobra.getName()) ||
					sumariser2.equals(VisibilityValues.dobra.getName()) ||
					sumariser3.equals(VisibilityValues.dobra.getName()) ||
					sumariser4.equals(VisibilityValues.dobra.getName())) {

				visibilityVariable.addTerm(VisibilityValues.dobra.getName(), VisibilityValues.dobra.getFuzzySet());

			} else if (sumariser1.equals(VisibilityValues.bardzoDobra.getName()) ||
					sumariser2.equals(VisibilityValues.bardzoDobra.getName()) ||
					sumariser3.equals(VisibilityValues.bardzoDobra.getName()) ||
					sumariser4.equals(VisibilityValues.bardzoDobra.getName())) {

				visibilityVariable.addTerm(VisibilityValues.bardzoDobra.getName(), VisibilityValues.bardzoDobra.getFuzzySet());
			}

			//List<LinguisticTerm> visibilityTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : visibilityVariable.getTerms().entrySet()) {
				LinguisticTerm visibilityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				visibilityTerm.setData(visibility);
				sumariser.add(visibilityTerm);
			}

			visibilityVariable.clearTerms(); // Opcjonalnie, jeśli chcesz wyczyścić po użyciu
		}


		// Indeks UV
		if (data1.equals(UvValues.uvIndexVariable.getName()) ||
				data2.equals(UvValues.uvIndexVariable.getName()) ||
				data3.equals(UvValues.uvIndexVariable.getName()) ||
				data4.equals(UvValues.uvIndexVariable.getName())) {

			LinguisticVariable uvVariable = UvValues.uvIndexVariable;

			if (sumariser1.equals(UvValues.niskie.getName()) ||
					sumariser2.equals(UvValues.niskie.getName()) ||
					sumariser3.equals(UvValues.niskie.getName()) ||
					sumariser4.equals(UvValues.niskie.getName())) {

				uvVariable.addTerm(UvValues.niskie.getName(), UvValues.niskie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.umiarkowane.getName()) ||
					sumariser2.equals(UvValues.umiarkowane.getName()) ||
					sumariser3.equals(UvValues.umiarkowane.getName()) ||
					sumariser4.equals(UvValues.umiarkowane.getName())) {

				uvVariable.addTerm(UvValues.umiarkowane.getName(), UvValues.umiarkowane.getFuzzySet());

			} else if (sumariser1.equals(UvValues.wysokie.getName()) ||
					sumariser2.equals(UvValues.wysokie.getName()) ||
					sumariser3.equals(UvValues.wysokie.getName()) ||
					sumariser4.equals(UvValues.wysokie.getName())) {

				uvVariable.addTerm(UvValues.wysokie.getName(), UvValues.wysokie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser2.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser3.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser4.equals(UvValues.bardzoWysokie.getName())) {

				uvVariable.addTerm(UvValues.bardzoWysokie.getName(), UvValues.bardzoWysokie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.ekstremalne.getName()) ||
					sumariser2.equals(UvValues.ekstremalne.getName()) ||
					sumariser3.equals(UvValues.ekstremalne.getName()) ||
					sumariser4.equals(UvValues.ekstremalne.getName())) {

				uvVariable.addTerm(UvValues.ekstremalne.getName(), UvValues.ekstremalne.getFuzzySet());
			}

			//List<LinguisticTerm> uvTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : uvVariable.getTerms().entrySet()) {
				LinguisticTerm uvTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				uvTerm.setData(uv);
				sumariser.add(uvTerm);
			}

			uvVariable.clearTerms(); // Opcjonalne czyszczenie po użyciu
		}


		// Jakość powietrza CO2
		if (data1.equals(CoValues.coQualityVariable.getName()) ||
				data2.equals(CoValues.coQualityVariable.getName()) ||
				data3.equals(CoValues.coQualityVariable.getName()) ||
				data4.equals(CoValues.coQualityVariable.getName())) {

			LinguisticVariable carbonVariable = CoValues.coQualityVariable;

			if (sumariser1.equals(CoValues.normalne.getName()) ||
					sumariser2.equals(CoValues.normalne.getName()) ||
					sumariser3.equals(CoValues.normalne.getName()) ||
					sumariser4.equals(CoValues.normalne.getName())) {

				carbonVariable.addTerm(CoValues.normalne.getName(), CoValues.normalne.getFuzzySet());

			} else if (sumariser1.equals(CoValues.wysokie.getName()) ||
					sumariser2.equals(CoValues.wysokie.getName()) ||
					sumariser3.equals(CoValues.wysokie.getName()) ||
					sumariser4.equals(CoValues.wysokie.getName())) {

				carbonVariable.addTerm(CoValues.wysokie.getName(), CoValues.wysokie.getFuzzySet());

			} else if (sumariser1.equals(CoValues.niezdrowe.getName()) ||
					sumariser2.equals(CoValues.niezdrowe.getName()) ||
					sumariser3.equals(CoValues.niezdrowe.getName()) ||
					sumariser4.equals(CoValues.niezdrowe.getName())) {

				carbonVariable.addTerm(CoValues.niezdrowe.getName(), CoValues.niezdrowe.getFuzzySet());

			} else if (sumariser1.equals(CoValues.niebezpieczne.getName()) ||
					sumariser2.equals(CoValues.niebezpieczne.getName()) ||
					sumariser3.equals(CoValues.niebezpieczne.getName()) ||
					sumariser4.equals(CoValues.niebezpieczne.getName())) {

				carbonVariable.addTerm(CoValues.niebezpieczne.getName(), CoValues.niebezpieczne.getFuzzySet());
			}

			//List<LinguisticTerm> carbonTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : carbonVariable.getTerms().entrySet()) {
				LinguisticTerm carbonTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				carbonTerm.setData(carbon);
				sumariser.add(carbonTerm);
			}

			carbonVariable.clearTerms(); // Wyczyść po użyciu (opcjonalnie, zależnie od logiki aplikacji)
		}


		// Jakość powietrza NO2
		if (data1.equals(NoValues.no2QualityVariable.getName()) ||
				data2.equals(NoValues.no2QualityVariable.getName()) ||
				data3.equals(NoValues.no2QualityVariable.getName()) ||
				data4.equals(NoValues.no2QualityVariable.getName())) {

			LinguisticVariable nitrogenVariable = NoValues.no2QualityVariable;

			if (sumariser1.equals(NoValues.normalne.getName()) ||
					sumariser2.equals(NoValues.normalne.getName()) ||
					sumariser3.equals(NoValues.normalne.getName()) ||
					sumariser4.equals(NoValues.normalne.getName())) {

				nitrogenVariable.addTerm(NoValues.normalne.getName(), NoValues.normalne.getFuzzySet());

			} else if (sumariser1.equals(NoValues.niezdrowe.getName()) ||
					sumariser2.equals(NoValues.niezdrowe.getName()) ||
					sumariser3.equals(NoValues.niezdrowe.getName()) ||
					sumariser4.equals(NoValues.niezdrowe.getName())) {

				nitrogenVariable.addTerm(NoValues.niezdrowe.getName(), NoValues.niezdrowe.getFuzzySet());

			} else if (sumariser1.equals(NoValues.niebezpieczne.getName()) ||
					sumariser2.equals(NoValues.niebezpieczne.getName()) ||
					sumariser3.equals(NoValues.niebezpieczne.getName()) ||
					sumariser4.equals(NoValues.niebezpieczne.getName())) {

				nitrogenVariable.addTerm(NoValues.niebezpieczne.getName(), NoValues.niebezpieczne.getFuzzySet());
			}

			//List<LinguisticTerm> nitrogenTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : nitrogenVariable.getTerms().entrySet()) {
				LinguisticTerm nitrogenTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				nitrogenTerm.setData(nitrogen);
				sumariser.add(nitrogenTerm);
			}

			nitrogenVariable.clearTerms(); // Opcjonalnie - jeśli nie chcesz, żeby termy zostały w pamięci
		}


		// Jakość powietrza
		if (data1.equals(AirValues.airQuality.getName()) ||
				data2.equals(AirValues.airQuality.getName()) ||
				data3.equals(AirValues.airQuality.getName()) ||
				data4.equals(AirValues.airQuality.getName())) {

			LinguisticVariable qualityVariable = AirValues.airQuality;

			if (sumariser1.equals(AirValues.bardzoDobra.getName()) ||
					sumariser2.equals(AirValues.bardzoDobra.getName()) ||
					sumariser3.equals(AirValues.bardzoDobra.getName()) ||
					sumariser4.equals(AirValues.bardzoDobra.getName())) {

				qualityVariable.addTerm(AirValues.bardzoDobra.getName(), AirValues.bardzoDobra.getFuzzySet());

			} else if (sumariser1.equals(AirValues.dobra.getName()) ||
					sumariser2.equals(AirValues.dobra.getName()) ||
					sumariser3.equals(AirValues.dobra.getName()) ||
					sumariser4.equals(AirValues.dobra.getName())) {

				qualityVariable.addTerm(AirValues.dobra.getName(), AirValues.dobra.getFuzzySet());

			} else if (sumariser1.equals(AirValues.umiarkowana.getName()) ||
					sumariser2.equals(AirValues.umiarkowana.getName()) ||
					sumariser3.equals(AirValues.umiarkowana.getName()) ||
					sumariser4.equals(AirValues.umiarkowana.getName())) {

				qualityVariable.addTerm(AirValues.umiarkowana.getName(), AirValues.umiarkowana.getFuzzySet());

			} else if (sumariser1.equals(AirValues.zla.getName()) ||
					sumariser2.equals(AirValues.zla.getName()) ||
					sumariser3.equals(AirValues.zla.getName()) ||
					sumariser4.equals(AirValues.zla.getName())) {

				qualityVariable.addTerm(AirValues.zla.getName(), AirValues.zla.getFuzzySet());

			} else if (sumariser1.equals(AirValues.bardzoZla.getName()) ||
					sumariser2.equals(AirValues.bardzoZla.getName()) ||
					sumariser3.equals(AirValues.bardzoZla.getName()) ||
					sumariser4.equals(AirValues.bardzoZla.getName())) {

				qualityVariable.addTerm(AirValues.bardzoZla.getName(), AirValues.bardzoZla.getFuzzySet());
			}

			//List<LinguisticTerm> airTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : qualityVariable.getTerms().entrySet()) {
				LinguisticTerm airTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				airTerm.setData(quality);
				sumariser.add(airTerm);
			}

			qualityVariable.clearTerms(); // Opcjonalne czyszczenie po zakończeniu
		}

		if (!data5.equals("Brak") && !qualificator.equals("Brak")) {
			if (data5.equals(TempValues.tempVariable.getName())) {
				LinguisticVariable tempVariable = TempValues.tempVariable;
				if (qualificator.equals(TempValues.bardzoZimna.getName())) {
					tempVariable.addTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
				} else if (qualificator.equals(TempValues.zimna.getName())) {
					tempVariable.addTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
				} else if (qualificator.equals(TempValues.umiarkowana.getName())) {
					tempVariable.addTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
				} else if (qualificator.equals(TempValues.ciepla.getName())) {
					tempVariable.addTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
				} else if (qualificator.equals(TempValues.goraca.getName())) {
					tempVariable.addTerm(TempValues.goraca.getName(), TempValues.goraca.getFuzzySet());
				}
				//List<LinguisticTerm> temperaturesTerms = new ArrayList<>();
				for (Map.Entry<String, FuzzySet> entry : tempVariable.getTerms().entrySet()) {
					LinguisticTerm temperatureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					temperatureTerm.setData(temperatures);
					qualifier.add(temperatureTerm);
				}
				tempVariable.clearTerms();
			}


		// Wilgotność
			if (data5.equals(HumidityValues.humidityVariable.getName())) {
				LinguisticVariable humidity = HumidityValues.humidityVariable;

				if (qualificator.equals(HumidityValues.suche.getName())) {
					humidity.addTerm(HumidityValues.suche.getName(), HumidityValues.suche.getFuzzySet());
				} else if (qualificator.equals(HumidityValues.umiarkowane.getName())) {
					humidity.addTerm(HumidityValues.umiarkowane.getName(), HumidityValues.umiarkowane.getFuzzySet());
				} else if (qualificator.equals(HumidityValues.wilgotne.getName())) {
					humidity.addTerm(HumidityValues.wilgotne.getName(), HumidityValues.wilgotne.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : humidity.getTerms().entrySet()) {
					LinguisticTerm humidityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					humidityTerm.setData(humidities);
					qualifier.add(humidityTerm);
				}

				humidity.clearTerms();
			}


			if (data5.equals(TimeValues.timeVariable.getName())) {
				LinguisticVariable timeVariable = TimeValues.timeVariable;

				if (qualificator.equals(TimeValues.nocna.getName())) {
					timeVariable.addTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());
				} else if (qualificator.equals(TimeValues.poranna.getName())) {
					timeVariable.addTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());
				} else if (qualificator.equals(TimeValues.poludniowa.getName())) {
					timeVariable.addTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());
				} else if (qualificator.equals(TimeValues.popoludniowa.getName())) {
					timeVariable.addTerm(TimeValues.popoludniowa.getName(), TimeValues.popoludniowa.getFuzzySet());
				} else if (qualificator.equals(TimeValues.wieczorna.getName())) {
					timeVariable.addTerm(TimeValues.wieczorna.getName(), TimeValues.wieczorna.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : timeVariable.getTerms().entrySet()) {
					LinguisticTerm timeTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					timeTerm.setData(hours);
					qualifier.add(timeTerm);
				}

				timeVariable.clearTerms(); // opcjonalnie, jeśli potrzebne
			}


		// Wiatr
			if (data5.equals(WindValues.windVariable.getName())) {
				LinguisticVariable windVariable = WindValues.windVariable;

				if (qualificator.equals(WindValues.slaby.getName())) {
					windVariable.addTerm(WindValues.slaby.getName(), WindValues.slaby.getFuzzySet());
				} else if (qualificator.equals(WindValues.umiarkowany.getName())) {
					windVariable.addTerm(WindValues.umiarkowany.getName(), WindValues.umiarkowany.getFuzzySet());
				} else if (qualificator.equals(WindValues.silny.getName())) {
					windVariable.addTerm(WindValues.silny.getName(), WindValues.silny.getFuzzySet());
				} else if (qualificator.equals(WindValues.bardzoSilny.getName())) {
					windVariable.addTerm(WindValues.bardzoSilny.getName(), WindValues.bardzoSilny.getFuzzySet());
				} else if (qualificator.equals(WindValues.gwaltowny.getName())) {
					windVariable.addTerm(WindValues.gwaltowny.getName(), WindValues.gwaltowny.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : windVariable.getTerms().entrySet()) {
					LinguisticTerm windTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					windTerm.setData(wind);
					qualifier.add(windTerm);
				}

				windVariable.clearTerms(); // opcjonalne
			}



		// Cisnienie
			if (data5.equals(PressureValues.pressureVariable.getName())) {
				LinguisticVariable pressureVariable = PressureValues.pressureVariable;

				if (qualificator.equals(PressureValues.niskie.getName())) {
					pressureVariable.addTerm(PressureValues.niskie.getName(), PressureValues.niskie.getFuzzySet());
				} else if (qualificator.equals(PressureValues.normalne.getName())) {
					pressureVariable.addTerm(PressureValues.normalne.getName(), PressureValues.normalne.getFuzzySet());
				} else if (qualificator.equals(PressureValues.wysokie.getName())) {
					pressureVariable.addTerm(PressureValues.wysokie.getName(), PressureValues.wysokie.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : pressureVariable.getTerms().entrySet()) {
					LinguisticTerm pressureTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					pressureTerm.setData(pressure);
					qualifier.add(pressureTerm);
				}

				pressureVariable.clearTerms(); // opcjonalne czyszczenie
			}



		// Widoczność
			if (data5.equals(VisibilityValues.visibilityVariable.getName())) {
				LinguisticVariable visibilityVariable = VisibilityValues.visibilityVariable;

				if (qualificator.equals(VisibilityValues.slaba.getName())) {
					visibilityVariable.addTerm(VisibilityValues.slaba.getName(), VisibilityValues.slaba.getFuzzySet());
				} else if (qualificator.equals(VisibilityValues.umiarkowana.getName())) {
					visibilityVariable.addTerm(VisibilityValues.umiarkowana.getName(), VisibilityValues.umiarkowana.getFuzzySet());
				} else if (qualificator.equals(VisibilityValues.dobra.getName())) {
					visibilityVariable.addTerm(VisibilityValues.dobra.getName(), VisibilityValues.dobra.getFuzzySet());
				} else if (qualificator.equals(VisibilityValues.bardzoDobra.getName())) {
					visibilityVariable.addTerm(VisibilityValues.bardzoDobra.getName(), VisibilityValues.bardzoDobra.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : visibilityVariable.getTerms().entrySet()) {
					LinguisticTerm visibilityTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					visibilityTerm.setData(visibility);
					qualifier.add(visibilityTerm);
				}

				visibilityVariable.clearTerms(); // opcjonalne czyszczenie
			}



		// Indeks UV
		if (data1.equals(UvValues.uvIndexVariable.getName()) ||
				data2.equals(UvValues.uvIndexVariable.getName()) ||
				data3.equals(UvValues.uvIndexVariable.getName()) ||
				data4.equals(UvValues.uvIndexVariable.getName())) {

			LinguisticVariable uvVariable = UvValues.uvIndexVariable;

			if (sumariser1.equals(UvValues.niskie.getName()) ||
					sumariser2.equals(UvValues.niskie.getName()) ||
					sumariser3.equals(UvValues.niskie.getName()) ||
					sumariser4.equals(UvValues.niskie.getName())) {

				uvVariable.addTerm(UvValues.niskie.getName(), UvValues.niskie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.umiarkowane.getName()) ||
					sumariser2.equals(UvValues.umiarkowane.getName()) ||
					sumariser3.equals(UvValues.umiarkowane.getName()) ||
					sumariser4.equals(UvValues.umiarkowane.getName())) {

				uvVariable.addTerm(UvValues.umiarkowane.getName(), UvValues.umiarkowane.getFuzzySet());

			} else if (sumariser1.equals(UvValues.wysokie.getName()) ||
					sumariser2.equals(UvValues.wysokie.getName()) ||
					sumariser3.equals(UvValues.wysokie.getName()) ||
					sumariser4.equals(UvValues.wysokie.getName())) {

				uvVariable.addTerm(UvValues.wysokie.getName(), UvValues.wysokie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser2.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser3.equals(UvValues.bardzoWysokie.getName()) ||
					sumariser4.equals(UvValues.bardzoWysokie.getName())) {

				uvVariable.addTerm(UvValues.bardzoWysokie.getName(), UvValues.bardzoWysokie.getFuzzySet());

			} else if (sumariser1.equals(UvValues.ekstremalne.getName()) ||
					sumariser2.equals(UvValues.ekstremalne.getName()) ||
					sumariser3.equals(UvValues.ekstremalne.getName()) ||
					sumariser4.equals(UvValues.ekstremalne.getName())) {

				uvVariable.addTerm(UvValues.ekstremalne.getName(), UvValues.ekstremalne.getFuzzySet());
			}

			//List<LinguisticTerm> uvTerms = new ArrayList<>();
			for (Map.Entry<String, FuzzySet> entry : uvVariable.getTerms().entrySet()) {
				LinguisticTerm uvTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
				uvTerm.setData(uv);
				sumariser.add(uvTerm);
			}

			uvVariable.clearTerms(); // Opcjonalne czyszczenie po użyciu
		}


		// Jakość powietrza CO2
			if (data5.equals(UvValues.uvIndexVariable.getName())) {
				LinguisticVariable uvVariable = UvValues.uvIndexVariable;

				if (qualificator.equals(UvValues.niskie.getName())) {
					uvVariable.addTerm(UvValues.niskie.getName(), UvValues.niskie.getFuzzySet());
				} else if (qualificator.equals(UvValues.umiarkowane.getName())) {
					uvVariable.addTerm(UvValues.umiarkowane.getName(), UvValues.umiarkowane.getFuzzySet());
				} else if (qualificator.equals(UvValues.wysokie.getName())) {
					uvVariable.addTerm(UvValues.wysokie.getName(), UvValues.wysokie.getFuzzySet());
				} else if (qualificator.equals(UvValues.bardzoWysokie.getName())) {
					uvVariable.addTerm(UvValues.bardzoWysokie.getName(), UvValues.bardzoWysokie.getFuzzySet());
				} else if (qualificator.equals(UvValues.ekstremalne.getName())) {
					uvVariable.addTerm(UvValues.ekstremalne.getName(), UvValues.ekstremalne.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : uvVariable.getTerms().entrySet()) {
					LinguisticTerm uvTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					uvTerm.setData(uv);
					qualifier.add(uvTerm);
				}

				uvVariable.clearTerms(); // Opcjonalne czyszczenie po użyciu
			}



		// Jakość powietrza NO2
			if (data5.equals(NoValues.no2QualityVariable.getName())) {
				LinguisticVariable nitrogenVariable = NoValues.no2QualityVariable;

				if (qualificator.equals(NoValues.normalne.getName())) {
					nitrogenVariable.addTerm(NoValues.normalne.getName(), NoValues.normalne.getFuzzySet());
				} else if (qualificator.equals(NoValues.niezdrowe.getName())) {
					nitrogenVariable.addTerm(NoValues.niezdrowe.getName(), NoValues.niezdrowe.getFuzzySet());
				} else if (qualificator.equals(NoValues.niebezpieczne.getName())) {
					nitrogenVariable.addTerm(NoValues.niebezpieczne.getName(), NoValues.niebezpieczne.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : nitrogenVariable.getTerms().entrySet()) {
					LinguisticTerm nitrogenTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					nitrogenTerm.setData(nitrogen);
					qualifier.add(nitrogenTerm);
				}

				nitrogenVariable.clearTerms(); // Opcjonalne czyszczenie po użyciu
			}



		// Jakość powietrza
			if (data5.equals(AirValues.airQuality.getName())) {
				LinguisticVariable qualityVariable = AirValues.airQuality;

				if (qualificator.equals(AirValues.bardzoDobra.getName())) {
					qualityVariable.addTerm(AirValues.bardzoDobra.getName(), AirValues.bardzoDobra.getFuzzySet());
				} else if (qualificator.equals(AirValues.dobra.getName())) {
					qualityVariable.addTerm(AirValues.dobra.getName(), AirValues.dobra.getFuzzySet());
				} else if (qualificator.equals(AirValues.umiarkowana.getName())) {
					qualityVariable.addTerm(AirValues.umiarkowana.getName(), AirValues.umiarkowana.getFuzzySet());
				} else if (qualificator.equals(AirValues.zla.getName())) {
					qualityVariable.addTerm(AirValues.zla.getName(), AirValues.zla.getFuzzySet());
				} else if (qualificator.equals(AirValues.bardzoZla.getName())) {
					qualityVariable.addTerm(AirValues.bardzoZla.getName(), AirValues.bardzoZla.getFuzzySet());
				}

				for (Map.Entry<String, FuzzySet> entry : qualityVariable.getTerms().entrySet()) {
					LinguisticTerm airTerm = new LinguisticTerm(entry.getKey(), entry.getValue());
					airTerm.setData(quality);
					qualifier.add(airTerm);
				}

				qualityVariable.clearTerms();
			}
	}

		List<LinguisticTerm> summarizers = new ArrayList<>();
		System.out.println(sumariser.size());
		summarizers.add(sumariser.get(0));
		if (sumariser.size() == 2) {
			summarizers.add(sumariser.get(1));
		}

		if (sumariser.size() == 3) {
			summarizers.add(sumariser.get(1));
			summarizers.add(sumariser.get(2));
		}

		if (sumariser.size() == 4) {
			summarizers.add(sumariser.get(1));
			summarizers.add(sumariser.get(2));
			summarizers.add(sumariser.get(3));
		}

		// Tutaj tworzysz i oceniasz podsumowanie
		if (data5.equals("Brak") && qualificator.equals("Brak")) {
			SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
					quantifierTerms.getFirst(),
					summarizers
			);

			System.out.println(singleSubjectSummary.summarization());
			singleSubjectSummary.print(weight);

		} else {
			LinguisticTerm qual = qualifier.getFirst();
			SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
					quantifierTerms.getFirst(),
					summarizers,
					qual
			);

			System.out.println(singleSubjectSummary.summarization());
			singleSubjectSummary.print(weight);
		}

	}

	public static void twoSubjectSummary(String data1, String data2, String data3, String data4, String data5, String sumariser1, String sumariser2, String sumariser3, String sumariser4, String qualificator, String quantificator, String cont1, String cont2) {
		// Kwalifikatory
		LinguisticVariable quatifiers = QuantifierValues.linguisticVariableQ;
		List<Quantifier> quantifierTerms = new ArrayList<>();
		if (quantificator.equals(QuantifierValues.Q1.getName())) {
			quatifiers.addTerm(QuantifierValues.Q1.getName(), QuantifierValues.Q1.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q1);
		} else if (quantificator.equals(QuantifierValues.Q2.getName())) {
			quatifiers.addTerm(QuantifierValues.Q2.getName(), QuantifierValues.Q2.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q2);
		} else if (quantificator.equals(QuantifierValues.Q3.getName())) {
			quatifiers.addTerm(QuantifierValues.Q3.getName(), QuantifierValues.Q3.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q3);
		} else if (quantificator.equals(QuantifierValues.Q4.getName())) {
			quatifiers.addTerm(QuantifierValues.Q4.getName(), QuantifierValues.Q4.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q4);
		} else if (quantificator.equals(QuantifierValues.Q5.getName())) {
			quatifiers.addTerm(QuantifierValues.Q5.getName(), QuantifierValues.Q5.getFuzzySet());
			quantifierTerms.add(QuantifierValues.Q5);
		}

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

		String continentA = cont1;
		String continentB = cont2;
		LinguisticTerm linguisticTerm1 = null;
		LinguisticTerm linguisticTerm2 = null;
		if (data1.equals(AirValues.airQuality.getName())) {
			linguisticTerm1 = getLinguisticTermAirQuality(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermAirQuality(sumariser1, continentB);
		} else if (data1.equals(CoValues.coQualityVariable.getName())) {
			linguisticTerm1 = getLinguisticTermCarbon(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermCarbon(sumariser1, continentB);
		} else if (data1.equals(HumidityValues.humidityVariable.getName())) {
			linguisticTerm1 = getLinguisticTermHumidity(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermHumidity(sumariser1, continentB);
		} else if (data1.equals(NoValues.no2QualityVariable.getName())) {
			linguisticTerm1 = getLinguisticTermNitrogen(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermNitrogen(sumariser1, continentB);
		} else if (data1.equals(PressureValues.pressureVariable.getName())) {
			linguisticTerm1 = getLinguisticTermPressure(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermPressure(sumariser1, continentB);
		} else if (data1.equals(TempValues.tempVariable.getName())) {
			linguisticTerm1 = getLinguisticTermTemp(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermTemp(sumariser1, continentB);
		} else if (data1.equals(WindValues.windVariable.getName())) {
			linguisticTerm1 = getLinguisticTermWind(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermWind(sumariser1, continentB);
		} else if (data1.equals(UvValues.uvIndexVariable.getName())) {
			linguisticTerm1 = getLinguisticTermUv(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermUv(sumariser1, continentB);
		} else if (data1.equals(VisibilityValues.visibilityVariable.getName())) {
			linguisticTerm1 = getLinguisticTermVisibility(sumariser1, continentA);
			linguisticTerm2 = getLinguisticTermVisibility(sumariser1, continentB);
		}

		LinguisticTerm linguisticTerm3 = null;
		LinguisticTerm linguisticTerm4 = null;

		if (!data5.equals("Brak")) {
			if (data5.equals(AirValues.airQuality.getName())) {
				linguisticTerm3 = getLinguisticTermAirQuality(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermAirQuality(qualificator, continentB);
			} else if (data5.equals(CoValues.coQualityVariable.getName())) {
				linguisticTerm3 = getLinguisticTermCarbon(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermCarbon(qualificator, continentB);
			} else if (data5.equals(HumidityValues.humidityVariable.getName())) {
				linguisticTerm3 = getLinguisticTermHumidity(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermHumidity(qualificator, continentB);
			} else if (data5.equals(NoValues.no2QualityVariable.getName())) {
				linguisticTerm3 = getLinguisticTermNitrogen(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermNitrogen(qualificator, continentB);
			} else if (data5.equals(PressureValues.pressureVariable.getName())) {
				linguisticTerm3 = getLinguisticTermPressure(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermPressure(qualificator, continentB);
			} else if (data5.equals(TempValues.tempVariable.getName())) {
				linguisticTerm3 = getLinguisticTermTemp(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermTemp(qualificator, continentB);
			} else if (data5.equals(WindValues.windVariable.getName())) {
				linguisticTerm3 = getLinguisticTermWind(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermWind(qualificator, continentB);
			} else if (data5.equals(UvValues.uvIndexVariable.getName())) {
				linguisticTerm3 = getLinguisticTermUv(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermUv(qualificator, continentB);
			} else if (data5.equals(VisibilityValues.visibilityVariable.getName())) {
				linguisticTerm3 = getLinguisticTermVisibility(qualificator, continentA);
				linguisticTerm4 = getLinguisticTermVisibility(qualificator, continentB);
			}
		}

		if (data5.equals("Brak") && qualificator.equals("Brak") && quantificator.equals("Brak")) {
			DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
					continentA,
					continentB,
					linguisticTerm1,
					linguisticTerm2
			);

			doubleSubjectSummary.fourthForm();
		} else if (!data5.equals("Brak") && !qualificator.equals("Brak") && !quantificator.equals("Brak")) {
			DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
					quantifierTerms.getFirst(),
					continentA,
					continentB,
					linguisticTerm1,
					linguisticTerm2,
					linguisticTerm3
			);

			doubleSubjectSummary.secondForm();

			DoubleSubjectSummary doubleSubjectSummary2 = new DoubleSubjectSummary(
					quantifierTerms.getFirst(),
					continentA,
					continentB,
					linguisticTerm1,
					linguisticTerm2,
					linguisticTerm4
			);
			doubleSubjectSummary2.thirdForm();

		} else {

			DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
					quantifierTerms.getFirst(),
					continentA,
					continentB,
					linguisticTerm1,
					linguisticTerm2
			);

			doubleSubjectSummary.firstForm();
			}
	}

	public static LinguisticTerm getLinguisticTermTemp(String term, String continent) {
        LinguisticTerm linguisticTerm;

		switch (term) {
            case "bardzo zimna" -> linguisticTerm = new LinguisticTerm(TempValues.bardzoZimna.getName(), TempValues.bardzoZimna.getFuzzySet());
            case "zimna" -> linguisticTerm = new LinguisticTerm(TempValues.zimna.getName(), TempValues.zimna.getFuzzySet());
            case "umiarkowana" -> linguisticTerm = new LinguisticTerm(TempValues.umiarkowana.getName(), TempValues.umiarkowana.getFuzzySet());
            case "ciepła" -> linguisticTerm = new LinguisticTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
            case "gorąca" -> linguisticTerm = new LinguisticTerm(TempValues.ciepla.getName(), TempValues.ciepla.getFuzzySet());
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

	public static LinguisticTerm getLinguisticTermWind(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "słaby" -> linguisticTerm = new LinguisticTerm(WindValues.slaby.getName(), WindValues.slaby.getFuzzySet());
			case "umiarkowany" -> linguisticTerm = new LinguisticTerm(WindValues.umiarkowany.getName(), WindValues.umiarkowany.getFuzzySet());
			case "silny" -> linguisticTerm = new LinguisticTerm(WindValues.silny.getName(), WindValues.silny.getFuzzySet());
			case "bardzo silny" -> linguisticTerm = new LinguisticTerm(WindValues.bardzoSilny.getName(), WindValues.bardzoSilny.getFuzzySet());
			case "gwałtowny" -> linguisticTerm = new LinguisticTerm(WindValues.gwaltowny.getName(), WindValues.gwaltowny.getFuzzySet());
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

	public static LinguisticTerm getLinguisticTermHumidity(String term, String continent) {
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

	public static LinguisticTerm getLinguisticTermPressure(String term, String continent) {
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

	public static LinguisticTerm getLinguisticTermVisibility(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "słaba" -> linguisticTerm = new LinguisticTerm(VisibilityValues.slaba.getName(), VisibilityValues.slaba.getFuzzySet());
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

	public static LinguisticTerm getLinguisticTermUv(String term, String continent) {
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

	public static LinguisticTerm getLinguisticTermCarbon(String term, String continent) {
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

	public static LinguisticTerm getLinguisticTermNitrogen(String term, String continent) {
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

	public static LinguisticTerm getLinguisticTermAirQuality(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "bardzo dobra" -> linguisticTerm = new LinguisticTerm(AirValues.bardzoDobra.getName(), AirValues.bardzoDobra.getFuzzySet());
			case "dobra" -> linguisticTerm = new LinguisticTerm(AirValues.dobra.getName(), AirValues.dobra.getFuzzySet());
			case "umiarkowana" -> linguisticTerm = new LinguisticTerm(AirValues.umiarkowana.getName(), AirValues.umiarkowana.getFuzzySet());
			case "zła" -> linguisticTerm = new LinguisticTerm(AirValues.zla.getName(), AirValues.zla.getFuzzySet());
			case "bardzo zła" -> linguisticTerm = new LinguisticTerm(AirValues.bardzoZla.getName(), AirValues.bardzoZla.getFuzzySet());
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

	public static LinguisticTerm getLinguisticTermTime(String term, String continent) {
		LinguisticTerm linguisticTerm;

		switch (term) {
			case "nocna" ->
					linguisticTerm = new LinguisticTerm(TimeValues.nocna.getName(), TimeValues.nocna.getFuzzySet());
			case "poranna" ->
					linguisticTerm = new LinguisticTerm(TimeValues.poranna.getName(), TimeValues.poranna.getFuzzySet());
			case "południowa" ->
					linguisticTerm = new LinguisticTerm(TimeValues.poludniowa.getName(), TimeValues.poludniowa.getFuzzySet());
			case "popołudniowa" ->
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

