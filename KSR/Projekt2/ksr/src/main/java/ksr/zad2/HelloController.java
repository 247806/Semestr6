package ksr.zad2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import ksr.zad2.fuzzy.DoubleSubjectSummary;
import ksr.zad2.fuzzy.LinguisticTerm;
import ksr.zad2.fuzzy.Quantifier;
import ksr.zad2.fuzzy.SingleSubjectSummary;
import ksr.zad2.model.Measurements;
import ksr.zad2.model.variables.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HelloController {

    @FXML public ComboBox<Quantifier> quantificator;
    @FXML public ComboBox<String> sub1;
    @FXML public ComboBox<String> sub2;
    @FXML public TextField t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11;
    @FXML private TreeView<String> summarizersTreeView;
    @FXML private TreeView<String> qualifiersTreeView;
    @FXML private Button generateSingleSummaryButton;
    @FXML private Button generateTwoSubjectSummaryButton;
    @FXML private TextArea summaryOutputArea;
    private final ObservableList<String> summaryItems = FXCollections.observableArrayList();

    @FXML
    private ListView<String> summaryListView;
    List<Object> allSummaries = new ArrayList<>();


    private final QuantifierValues quantifierValues = new QuantifierValues();

    @FXML
    private void initialize() {
        summarizersTreeView.setRoot(createSummarizersTree());
        summarizersTreeView.setShowRoot(false);
        summarizersTreeView.setCellFactory(createTreeCell());

        qualifiersTreeView.setRoot(createQualifiersTree());
        qualifiersTreeView.setShowRoot(false);
        qualifiersTreeView.setCellFactory(createTreeCell());

        initializeQuantifiers();
        initializeSubjects();

        t1.setText("0.30");
        t2.setText("0.07");
        t3.setText("0.07");
        t4.setText("0.07");
        t5.setText("0.07");
        t6.setText("0.07");
        t7.setText("0.07");
        t8.setText("0.07");
        t9.setText("0.07");
        t10.setText("0.07");
        t11.setText("0.07");

        summaryListView.setItems(summaryItems);

        summaryListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < allSummaries.size()) {
                Object selected = allSummaries.get(index);
                if (selected instanceof SingleSubjectSummary s) {
                    summaryOutputArea.setText(s.print());
                } else if (selected instanceof DoubleSubjectSummary d) {
                    summaryOutputArea.setText(d.getSummary());
                }
            }
        });

    }

    private void initializeQuantifiers() {
        // Pobierz wszystkie kwantyfikatory z klasy QuantifierValues
        List<Quantifier> quantifiers = QuantifierValues.getAll();

        // Ustaw listę kwantyfikatorów w ComboBox
        quantificator.getItems().addAll(quantifiers);

        // Ustaw wyświetlanie nazw kwantyfikatorów w ComboBox
        quantificator.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Quantifier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        quantificator.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Quantifier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }


    private void initializeSubjects() {
        // Lista podmiotów do wyboru
        List<String> subjects = List.of("Europe", "Africa", "America", "Asia");

        // Ustaw listę podmiotów w ComboBoxach sub1 i sub2
        sub1.getItems().addAll(subjects);
        sub2.getItems().addAll(subjects);
    }


    private CheckBoxTreeItem<String> createSummarizersTree() {
        CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>("Sumaryzatory");
        CheckBoxTreeItem<String> airQualityNode = new CheckBoxTreeItem<>(AirValues.airQuality.getName());

        airQualityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(AirValues.bardzoDobra.getName()),
                new CheckBoxTreeItem<>(AirValues.dobra.getName()),
                new CheckBoxTreeItem<>(AirValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(AirValues.zla.getName()),
                new CheckBoxTreeItem<>(AirValues.bardzoZla.getName())
        );
        CheckBoxTreeItem<String> pressureNode = new CheckBoxTreeItem<>(PressureValues.pressureVariable.getName());
        pressureNode.getChildren().addAll(
                new CheckBoxTreeItem<>(PressureValues.niskie.getName()),
                new CheckBoxTreeItem<>(PressureValues.normalne.getName()),
                new CheckBoxTreeItem<>(PressureValues.wysokie.getName())
        );

        CheckBoxTreeItem<String> humidityNode = new CheckBoxTreeItem<>(HumidityValues.humidityVariable.getName());
        humidityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(HumidityValues.suche.getName()),
                new CheckBoxTreeItem<>(HumidityValues.umiarkowane.getName()),
                new CheckBoxTreeItem<>(HumidityValues.wilgotne.getName())
        );

        CheckBoxTreeItem<String> visibilityNode = new CheckBoxTreeItem<>(VisibilityValues.visibilityVariable.getName());
        visibilityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(VisibilityValues.slaba.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.dobra.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.bardzoDobra.getName())
        );

        CheckBoxTreeItem<String> tempNode = new CheckBoxTreeItem<>(TempValues.tempVariable.getName());
        tempNode.getChildren().addAll(
                new CheckBoxTreeItem<>(TempValues.bardzoZimna.getName()),
                new CheckBoxTreeItem<>(TempValues.zimna.getName()),
                new CheckBoxTreeItem<>(TempValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(TempValues.ciepla.getName()),
                new CheckBoxTreeItem<>(TempValues.goraca.getName())
        );

        CheckBoxTreeItem<String> windNode = new CheckBoxTreeItem<>(WindValues.windVariable.getName());
        windNode.getChildren().addAll(
                new CheckBoxTreeItem<>(WindValues.slaby.getName()),
                new CheckBoxTreeItem<>(WindValues.umiarkowany.getName()),
                new CheckBoxTreeItem<>(WindValues.silny.getName()),
                new CheckBoxTreeItem<>(WindValues.bardzoSilny.getName()),
                new CheckBoxTreeItem<>(WindValues.gwaltowny.getName())
        );

        CheckBoxTreeItem<String> no2Node = new CheckBoxTreeItem<>(NoValues.no2QualityVariable.getName());
        no2Node.getChildren().addAll(
                new CheckBoxTreeItem<>(NoValues.normalne.getName()),
                new CheckBoxTreeItem<>(NoValues.niezdrowe.getName()),
                new CheckBoxTreeItem<>(NoValues.niebezpieczne.getName())
        );

        CheckBoxTreeItem<String> coNode = new CheckBoxTreeItem<>(CoValues.coQualityVariable.getName());
        coNode.getChildren().addAll(
                new CheckBoxTreeItem<>(CoValues.normalne.getName()),
                new CheckBoxTreeItem<>(CoValues.wysokie.getName()),
                new CheckBoxTreeItem<>(CoValues.niezdrowe.getName()),
                new CheckBoxTreeItem<>(CoValues.niebezpieczne.getName())
        );

        CheckBoxTreeItem<String> timeNode = new CheckBoxTreeItem<>(TimeValues.timeVariable.getName());
        timeNode.getChildren().addAll(
                new CheckBoxTreeItem<>(TimeValues.nocna.getName()),
                new CheckBoxTreeItem<>(TimeValues.poranna.getName()),
                new CheckBoxTreeItem<>(TimeValues.poludniowa.getName()),
                new CheckBoxTreeItem<>(TimeValues.popoludniowa.getName()),
                new CheckBoxTreeItem<>(TimeValues.wieczorna.getName())
        );

        CheckBoxTreeItem<String> uvNode = new CheckBoxTreeItem<>(UvValues.uvIndexVariable.getName());
        uvNode.getChildren().addAll(
                new CheckBoxTreeItem<>(UvValues.niskie.getName()),
                new CheckBoxTreeItem<>(UvValues.umiarkowane.getName()),
                new CheckBoxTreeItem<>(UvValues.wysokie.getName()),
                new CheckBoxTreeItem<>(UvValues.bardzoWysokie.getName()),
                new CheckBoxTreeItem<>(UvValues.ekstremalne.getName())
        );

// Dodaj wszystkie węzły do głównego drzewa
        root.getChildren().addAll(
                airQualityNode,
                pressureNode,
                humidityNode,
                visibilityNode,
                tempNode,
                windNode,
                no2Node,
                coNode,
                timeNode,
                uvNode
        );

        return root;

    }

    private CheckBoxTreeItem<String> createQualifiersTree() {
        CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>("Kwalifikatory");
        CheckBoxTreeItem<String> tempNode = new CheckBoxTreeItem<>(TempValues.tempVariable.getName());

        tempNode.getChildren().addAll(
                new CheckBoxTreeItem<>(TempValues.bardzoZimna.getName()),
                new CheckBoxTreeItem<>(TempValues.zimna.getName()),
                new CheckBoxTreeItem<>(TempValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(TempValues.ciepla.getName()),
                new CheckBoxTreeItem<>(TempValues.goraca.getName())
        );

        CheckBoxTreeItem<String> airQualityNode = new CheckBoxTreeItem<>(AirValues.airQuality.getName());

        airQualityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(AirValues.bardzoDobra.getName()),
                new CheckBoxTreeItem<>(AirValues.dobra.getName()),
                new CheckBoxTreeItem<>(AirValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(AirValues.zla.getName()),
                new CheckBoxTreeItem<>(AirValues.bardzoZla.getName())
        );
        CheckBoxTreeItem<String> pressureNode = new CheckBoxTreeItem<>(PressureValues.pressureVariable.getName());
        pressureNode.getChildren().addAll(
                new CheckBoxTreeItem<>(PressureValues.niskie.getName()),
                new CheckBoxTreeItem<>(PressureValues.normalne.getName()),
                new CheckBoxTreeItem<>(PressureValues.wysokie.getName())
        );

        CheckBoxTreeItem<String> humidityNode = new CheckBoxTreeItem<>(HumidityValues.humidityVariable.getName());
        humidityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(HumidityValues.suche.getName()),
                new CheckBoxTreeItem<>(HumidityValues.umiarkowane.getName()),
                new CheckBoxTreeItem<>(HumidityValues.wilgotne.getName())
        );

        CheckBoxTreeItem<String> visibilityNode = new CheckBoxTreeItem<>(VisibilityValues.visibilityVariable.getName());
        visibilityNode.getChildren().addAll(
                new CheckBoxTreeItem<>(VisibilityValues.slaba.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.umiarkowana.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.dobra.getName()),
                new CheckBoxTreeItem<>(VisibilityValues.bardzoDobra.getName())
        );

        CheckBoxTreeItem<String> windNode = new CheckBoxTreeItem<>(WindValues.windVariable.getName());
        windNode.getChildren().addAll(
                new CheckBoxTreeItem<>(WindValues.slaby.getName()),
                new CheckBoxTreeItem<>(WindValues.umiarkowany.getName()),
                new CheckBoxTreeItem<>(WindValues.silny.getName()),
                new CheckBoxTreeItem<>(WindValues.bardzoSilny.getName()),
                new CheckBoxTreeItem<>(WindValues.gwaltowny.getName())
        );

        CheckBoxTreeItem<String> no2Node = new CheckBoxTreeItem<>(NoValues.no2QualityVariable.getName());
        no2Node.getChildren().addAll(
                new CheckBoxTreeItem<>(NoValues.normalne.getName()),
                new CheckBoxTreeItem<>(NoValues.niezdrowe.getName()),
                new CheckBoxTreeItem<>(NoValues.niebezpieczne.getName())
        );

        CheckBoxTreeItem<String> coNode = new CheckBoxTreeItem<>(CoValues.coQualityVariable.getName());
        coNode.getChildren().addAll(
                new CheckBoxTreeItem<>(CoValues.normalne.getName()),
                new CheckBoxTreeItem<>(CoValues.wysokie.getName()),
                new CheckBoxTreeItem<>(CoValues.niezdrowe.getName()),
                new CheckBoxTreeItem<>(CoValues.niebezpieczne.getName())
        );

        CheckBoxTreeItem<String> timeNode = new CheckBoxTreeItem<>(TimeValues.timeVariable.getName());
        timeNode.getChildren().addAll(
                new CheckBoxTreeItem<>(TimeValues.nocna.getName()),
                new CheckBoxTreeItem<>(TimeValues.poranna.getName()),
                new CheckBoxTreeItem<>(TimeValues.poludniowa.getName()),
                new CheckBoxTreeItem<>(TimeValues.popoludniowa.getName()),
                new CheckBoxTreeItem<>(TimeValues.wieczorna.getName())
        );

        CheckBoxTreeItem<String> uvNode = new CheckBoxTreeItem<>(UvValues.uvIndexVariable.getName());
        uvNode.getChildren().addAll(
                new CheckBoxTreeItem<>(UvValues.niskie.getName()),
                new CheckBoxTreeItem<>(UvValues.umiarkowane.getName()),
                new CheckBoxTreeItem<>(UvValues.wysokie.getName()),
                new CheckBoxTreeItem<>(UvValues.bardzoWysokie.getName()),
                new CheckBoxTreeItem<>(UvValues.ekstremalne.getName())
        );

// Dodaj wszystkie węzły do głównego drzewa
        root.getChildren().addAll(
                airQualityNode,
                pressureNode,
                humidityNode,
                visibilityNode,
                tempNode,
                windNode,
                no2Node,
                coNode,
                timeNode,
                uvNode
        );
        return root;
    }

    private Callback<TreeView<String>, TreeCell<String>> createTreeCell() {
        return tree -> new CheckBoxTreeCell<>();
    }

    @FXML
    protected void onGenerateSingleSummary() {
        Quantifier quantifier = quantificator.getValue();
        List<LinguisticTerm> summarizer = getSelectedSummarizers();
        List<LinguisticTerm> qualifier = getSelectedQualifiers();
        List<String> selectedLinguisticVariableNames = summarizersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .filter(variableNode -> variableNode.getChildren().stream() // sprawdzamy, czy jakiekolwiek dziecko jest zaznaczone
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .anyMatch(CheckBoxTreeItem::isSelected))
                .map(CheckBoxTreeItem::getValue) // pobieramy nazwę zmiennej lingwistycznej
                .toList();

        List<String> selectedLinguisticVariableNamesQualifier = qualifiersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .filter(variableNode -> variableNode.getChildren().stream() // sprawdzamy, czy jakiekolwiek dziecko jest zaznaczone
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .anyMatch(CheckBoxTreeItem::isSelected))
                .map(CheckBoxTreeItem::getValue) // pobieramy nazwę zmiennej lingwistycznej
                .toList();

        List<Double> weights = List.of(
                Double.parseDouble(t1.getText()),
                Double.parseDouble(t2.getText()),
                Double.parseDouble(t3.getText()),
                Double.parseDouble(t4.getText()),
                Double.parseDouble(t5.getText()),
                Double.parseDouble(t6.getText()),
                Double.parseDouble(t7.getText()),
                Double.parseDouble(t8.getText()),
                Double.parseDouble(t9.getText()),
                Double.parseDouble(t10.getText()),
                Double.parseDouble(t11.getText())
        );

        if (quantifier == null || summarizer.isEmpty()) {
            summaryOutputArea.setText("Proszę uzupełnić wszystkie pola i wybrać sumaryzator.");
            return;
        }

        if (summarizer.size() > 4) {
            summaryOutputArea.setText("Proszę wybrać maksymalnie 4 sumaryzatory.");
            return;
        }

        if (qualifier.size() > 1) {
            summaryOutputArea.setText("Proszę wybrać maksymalnie 1 kwalifikator.");
            return;
        }

        if (qualifier.size() == 1) {
            qualifier.getFirst().setData(addData(selectedLinguisticVariableNamesQualifier.getFirst()));
            System.out.println("Qualifiers: " + qualifier.getFirst().getName() + " - " + qualifier.getFirst().getData().size());
            System.out.println("Selected Variables: " + selectedLinguisticVariableNamesQualifier.getFirst());
            for (int i = 0; i < selectedLinguisticVariableNames.size(); i++) {
                String variableName = selectedLinguisticVariableNames.get(i);
                List<Double> data = addData(variableName);
                if (data == null) {
                    summaryOutputArea.setText("Nie znaleziono danych dla zmiennej: " + variableName);
                    return;
                }
                summarizer.get(i).setData(data);
            }

            SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
                    quantifier,
                    summarizer,
                    qualifier.getFirst()
            );
            singleSubjectSummary.setWeights(weights);
            summaryOutputArea.setText(singleSubjectSummary.summarization());
            allSummaries.add(singleSubjectSummary);
            singleSubjectSummary.print();
            summaryListView.getItems().add(singleSubjectSummary.getSummaryText());
        } else {
            for (int i = 0; i < selectedLinguisticVariableNames.size(); i++) {
                String variableName = selectedLinguisticVariableNames.get(i);
                List<Double> data = addData(variableName);
                if (data == null) {
                    summaryOutputArea.setText("Nie znaleziono danych dla zmiennej: " + variableName);
                    return;
                }
                summarizer.get(i).setData(data);
            }

            SingleSubjectSummary singleSubjectSummary = new SingleSubjectSummary(
                    quantifier,
                    summarizer
            );
            singleSubjectSummary.setWeights(weights);
            summaryOutputArea.setText(singleSubjectSummary.summarization());
            allSummaries.add(singleSubjectSummary);
            singleSubjectSummary.print();
            summaryListView.getItems().add(singleSubjectSummary.getSummaryText());

        }
    }

    private List<Double> addData(String name) {
        List<Measurements> measurements  = KsrApplication.measurementsRepository.findAll();
        switch (name) {
            case "Jakość powietrza":
                return measurements.stream()
                        .map(Measurements::getAir_quality_gb_defra_index)// jeśli metoda zwraca int
                        .toList();
            case "Ciśnienie":
                return measurements.stream()
                        .map(Measurements::getPressure_mb)
                        .toList();
            case "Wilgotność powietrza":
                return measurements.stream()
                        .map(Measurements::getHumidity)
                        .toList();
            case "Stopień widoczności":
                return measurements.stream()
                        .map(Measurements::getVisibility_km)
                        .toList();
            case "Temperatura":
                return measurements.stream()
                        .map(Measurements::getTemperature_celsius)
                        .toList();
            case "Wiatr":
                return measurements.stream()
                        .map(Measurements::getWind_kph)
                        .toList();

            case "Zanieczyszczenie NO2":
                return measurements.stream()
                        .map(Measurements::getAir_quality_Nitrogen_Dioxide)
                        .toList();

            case "Pora dnia":
                return measurements.stream()
                        .map(Measurements::getLast_updated)
                        .map(date -> (double) date.getHour() + date.getMinute() / 60.0)
                        .toList();

            case "Promieniowanie UV":
                return measurements.stream().map(Measurements::getUv_index).toList();

            case "Zanieczyszczenie CO":
                return measurements.stream()
                        .map(Measurements::getAir_quality_Carbon_Monoxide)
                        .toList();
        }
        return null;
    }

    @FXML
    protected void onGenerateTwoSubjectSummary() {
        Quantifier quantifier = quantificator.getValue();
        String subject1 = sub1.getValue();
        String subject2 = sub2.getValue();
        List<LinguisticTerm> summarizer = getSelectedSummarizers();
        List<LinguisticTerm> qualifier = getSelectedQualifiers();

        List<String> selectedLinguisticVariableNames = summarizersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .filter(variableNode -> variableNode.getChildren().stream() // sprawdzamy, czy jakiekolwiek dziecko jest zaznaczone
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .anyMatch(CheckBoxTreeItem::isSelected))
                .map(CheckBoxTreeItem::getValue) // pobieramy nazwę zmiennej lingwistycznej
                .toList();

        List<String> selectedLinguisticVariableNamesQualifier = qualifiersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .filter(variableNode -> variableNode.getChildren().stream() // sprawdzamy, czy jakiekolwiek dziecko jest zaznaczone
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .anyMatch(CheckBoxTreeItem::isSelected))
                .map(CheckBoxTreeItem::getValue) // pobieramy nazwę zmiennej lingwistycznej
                .toList();

        if (subject1 == null || subject2 == null || summarizer == null) {
            summaryOutputArea.setText("Proszę uzupełnić wszystkie pola i wybrać sumaryzator.");
            return;
        }

        if (quantifier == null) {
            LinguisticTerm summarizer1 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());
            LinguisticTerm summarizer2 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());

            System.out.println(summarizer1.getName() + " - " + summarizer2.getName());
            System.out.println(selectedLinguisticVariableNames.getFirst());

            summarizer1.setData(setDataByContinent(subject1, selectedLinguisticVariableNames.getFirst()));
            System.out.println("Pierwszy " + summarizer1.getData().size());
            summarizer2.setData(setDataByContinent(subject2, selectedLinguisticVariableNames.getFirst()));

            System.out.println(summarizer1.getData().size() + " - " + summarizer2.getData().size());

            DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
                    subject1,
                    subject2,
                    summarizer1,
                    summarizer2
            );

            doubleSubjectSummary.fourthForm();
            summaryOutputArea.setText(doubleSubjectSummary.getSummary());
            allSummaries.add(doubleSubjectSummary);
            summaryListView.getItems().add(doubleSubjectSummary.getSummary());
        } else if (qualifier.size() == 1) {
            LinguisticTerm summarizer1 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());
            LinguisticTerm summarizer2 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());
//            qualifier.getFirst().setData(setDataByContinent(subject1, selectedLinguisticVariableNamesQualifier.getFirst()));

            summarizer1.setData(setDataByContinent(subject1, selectedLinguisticVariableNames.getFirst()));
            summarizer2.setData(setDataByContinent(subject2, selectedLinguisticVariableNames.getFirst()));

//            DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
//                    quantifier,
//                    subject1,
//                    subject2,
//                    summarizer1,
//                    summarizer2,
//                    qualifier.getFirst()
//            );
//            doubleSubjectSummary.thirdForm();
//            summaryOutputArea.setText(doubleSubjectSummary.getSummary());
//            doubleSubjectSummaries.add(doubleSubjectSummary);

            qualifier.getFirst().setData(setDataByContinent(subject2, selectedLinguisticVariableNames.getFirst()));
            DoubleSubjectSummary doubleSubjectSummary2 = new DoubleSubjectSummary(
                    quantifier,
                    subject1,
                    subject2,
                    summarizer1,
                    summarizer2,
                    qualifier.getFirst()
            );
            doubleSubjectSummary2.secondForm();
            summaryOutputArea.setText(doubleSubjectSummary2.getSummary());
            allSummaries.add(doubleSubjectSummary2);
            summaryListView.getItems().add(doubleSubjectSummary2.getSummary());
        } else {
            LinguisticTerm summarizer1 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());
            LinguisticTerm summarizer2 = new LinguisticTerm(summarizer.getFirst().getName(), summarizer.getFirst().getFuzzySet());

            summarizer1.setData(setDataByContinent(subject1, selectedLinguisticVariableNames.getFirst()));
            summarizer2.setData(setDataByContinent(subject2, selectedLinguisticVariableNames.getFirst()));

            DoubleSubjectSummary doubleSubjectSummary = new DoubleSubjectSummary(
                    quantifier,
                    subject1,
                    subject2,
                    summarizer1,
                    summarizer2
            );

            doubleSubjectSummary.firstForm();
            summaryOutputArea.setText(doubleSubjectSummary.getSummary());
            allSummaries.add(doubleSubjectSummary);
            summaryListView.getItems().add(doubleSubjectSummary.getSummary());
        }
    }

    private List<Double> setDataByContinent(String continent, String name) {
        System.out.println("Continent: " + continent + ", Variable: " + name);
        List<Measurements> measurements = KsrApplication.measurementsRepository.findByContinent(continent);
        return measurements.stream()
                .map(measurement -> switch (name) {
                    case "Jakość powietrza" -> measurement.getAir_quality_gb_defra_index();
                    case "Ciśnienie" -> measurement.getPressure_mb();
                    case "Wilgotność powietrza" -> measurement.getHumidity();
                    case "Stopień widoczności" -> measurement.getVisibility_km();
                    case "Temperatura" -> measurement.getTemperature_celsius();
                    case "Wiatr" -> measurement.getWind_kph();
                    case "Zanieczyszczenie NO2" -> measurement.getAir_quality_Nitrogen_Dioxide();
                    case "Pora dnia" ->
                            (double) measurement.getLast_updated().getHour() + measurement.getLast_updated().getMinute() / 60.0;
                    case "Promieniowanie UV" -> measurement.getUv_index();
                    case "Zanieczyszczenie CO" -> measurement.getAir_quality_Carbon_Monoxide();
                    default -> null;
                })
                .toList();
    }

    private List<LinguisticTerm> getSelectedSummarizers() {
        return summarizersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .flatMap(variableNode -> variableNode.getChildren().stream() // Iterujemy nad dziećmi danego węzła
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .filter(CheckBoxTreeItem::isSelected)
                        .map(childNode -> findLinguisticTermByName(
                                childNode.getValue(), // Nazwa sumaryzatora
                                variableNode.getValue() // Nazwa zmiennej lingwistycznej (obiekt nadrzędny)
                        )))
                .toList();
    }

    private List<LinguisticTerm> getSelectedQualifiers() {
        return qualifiersTreeView.getRoot().getChildren().stream()
                .map(node -> (CheckBoxTreeItem<String>) node)
                .flatMap(variableNode -> variableNode.getChildren().stream() // Iterujemy nad dziećmi danego węzła
                        .map(childNode -> (CheckBoxTreeItem<String>) childNode)
                        .filter(CheckBoxTreeItem::isSelected)
                        .map(childNode -> findLinguisticTermByName(
                                childNode.getValue(), // Nazwa kwalifikatora
                                variableNode.getValue() // Nazwa zmiennej lingwistycznej (obiekt nadrzędny)
                        )))
                .toList();
    }


    private LinguisticTerm findLinguisticTermByName(String nameSummarizer, String linguisticVariable) {
        switch (linguisticVariable) {
            case "Jakość powietrza":
                if (nameSummarizer.equals(AirValues.bardzoDobra.getName())) {
                    return AirValues.bardzoDobra;
                } else if (nameSummarizer.equals(AirValues.dobra.getName())) {
                    return AirValues.dobra;
                } else if (nameSummarizer.equals(AirValues.umiarkowana.getName())) {
                    return AirValues.umiarkowana;
                } else if (nameSummarizer.equals(AirValues.zla.getName())) {
                    return AirValues.zla;
                } else if (nameSummarizer.equals(AirValues.bardzoZla.getName())) {
                    return AirValues.bardzoZla;
                }
                break;

            case "Ciśnienie":
                if (nameSummarizer.equals(PressureValues.niskie.getName())) {
                    return PressureValues.niskie;
                } else if (nameSummarizer.equals(PressureValues.normalne.getName())) {
                    return PressureValues.normalne;
                } else if (nameSummarizer.equals(PressureValues.wysokie.getName())) {
                    return PressureValues.wysokie;
                }
                break;

            case "Wilgotność powietrza":
                if (nameSummarizer.equals(HumidityValues.suche.getName())) {
                    return HumidityValues.suche;
                } else if (nameSummarizer.equals(HumidityValues.umiarkowane.getName())) {
                    return HumidityValues.umiarkowane;
                } else if (nameSummarizer.equals(HumidityValues.wilgotne.getName())) {
                    return HumidityValues.wilgotne;
                }
                break;

            case "Stopień widoczności":
                if (nameSummarizer.equals(VisibilityValues.slaba.getName())) {
                    return VisibilityValues.slaba;
                } else if (nameSummarizer.equals(VisibilityValues.umiarkowana.getName())) {
                    return VisibilityValues.umiarkowana;
                } else if (nameSummarizer.equals(VisibilityValues.dobra.getName())) {
                    return VisibilityValues.dobra;
                } else if (nameSummarizer.equals(VisibilityValues.bardzoDobra.getName())) {
                    return VisibilityValues.bardzoDobra;
                }
                break;

            case "Temperatura":
                if (nameSummarizer.equals(TempValues.bardzoZimna.getName())) {
                    return TempValues.bardzoZimna;
                } else if (nameSummarizer.equals(TempValues.zimna.getName())) {
                    return TempValues.zimna;
                } else if (nameSummarizer.equals(TempValues.umiarkowana.getName())) {
                    return TempValues.umiarkowana;
                } else if (nameSummarizer.equals(TempValues.ciepla.getName())) {
                    return TempValues.ciepla;
                } else if (nameSummarizer.equals(TempValues.goraca.getName())) {
                    return TempValues.goraca;
                }
                break;

            case "Wiatr":
                if (nameSummarizer.equals(WindValues.slaby.getName())) {
                    return WindValues.slaby;
                } else if (nameSummarizer.equals(WindValues.umiarkowany.getName())) {
                    return WindValues.umiarkowany;
                } else if (nameSummarizer.equals(WindValues.silny.getName())) {
                    return WindValues.silny;
                } else if (nameSummarizer.equals(WindValues.bardzoSilny.getName())) {
                    return WindValues.bardzoSilny;
                } else if (nameSummarizer.equals(WindValues.gwaltowny.getName())) {
                    return WindValues.gwaltowny;
                }
                break;

            case "Zanieczyszczenie NO2":
                if (nameSummarizer.equals(NoValues.normalne.getName())) {
                    return NoValues.normalne;
                } else if (nameSummarizer.equals(NoValues.niezdrowe.getName())) {
                    return NoValues.niezdrowe;
                } else if (nameSummarizer.equals(NoValues.niebezpieczne.getName())) {
                    return NoValues.niebezpieczne;
                }
                break;

            case "Pora dnia":
                if (nameSummarizer.equals(TimeValues.nocna.getName())) {
                    return TimeValues.nocna;
                } else if (nameSummarizer.equals(TimeValues.poranna.getName())) {
                    return TimeValues.poranna;
                } else if (nameSummarizer.equals(TimeValues.poludniowa.getName())) {
                    return TimeValues.poludniowa;
                } else if (nameSummarizer.equals(TimeValues.popoludniowa.getName())) {
                    return TimeValues.popoludniowa;
                } else if (nameSummarizer.equals(TimeValues.wieczorna.getName())) {
                    return TimeValues.wieczorna;
                }
                break;

            case "Promieniowanie UV":
                if (nameSummarizer.equals(UvValues.niskie.getName())) {
                    return UvValues.niskie;
                } else if (nameSummarizer.equals(UvValues.umiarkowane.getName())) {
                    return UvValues.umiarkowane;
                } else if (nameSummarizer.equals(UvValues.wysokie.getName())) {
                    return UvValues.wysokie;
                } else if (nameSummarizer.equals(UvValues.bardzoWysokie.getName())) {
                    return UvValues.bardzoWysokie;
                } else if (nameSummarizer.equals(UvValues.ekstremalne.getName())) {
                    return UvValues.ekstremalne;
                }
                break;

            case "Zanieczyszczenie CO":
                if (nameSummarizer.equals(CoValues.normalne.getName())) {
                    return CoValues.normalne;
                } else if (nameSummarizer.equals(CoValues.wysokie.getName())) {
                    return CoValues.wysokie;
                } else if (nameSummarizer.equals(CoValues.niezdrowe.getName())) {
                    return CoValues.niezdrowe;
                } else if (nameSummarizer.equals(CoValues.niebezpieczne.getName())) {
                    return CoValues.niebezpieczne;
                }
                break;
        }
        return null;

    }


    @FXML
    protected void onHelloButtonClick() {
        summarizersTreeView.getRoot().getChildren().stream()
                .map(child -> (CheckBoxTreeItem<String>) child)
                .filter(CheckBoxTreeItem::isSelected)
                .forEach(selectedItem ->
                        System.out.println("Wybrany sumaryzator: " + selectedItem.getValue()));

        qualifiersTreeView.getRoot().getChildren().stream()
                .map(child -> (CheckBoxTreeItem<String>) child)
                .filter(CheckBoxTreeItem::isSelected)
                .forEach(selectedItem ->
                        System.out.println("Wybrany kwalifikator: " + selectedItem.getValue()));
    }

    @FXML
    public void onAdminButtonClick(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ksr/admin-view.fxml"));
            fxmlLoader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent adminView = fxmlLoader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(adminView));
            stage.setTitle("Panel Admina");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
