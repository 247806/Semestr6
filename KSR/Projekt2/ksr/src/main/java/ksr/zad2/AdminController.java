package ksr.zad2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

// Musi być komponent Springa
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ksr.zad2.fuzzy.*;
import ksr.zad2.model.variables.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AdminController {

    @FXML
    private TreeView<String> linguisticTreeView;

    @FXML
    private ListView<String> summarizerListView;

    @FXML
    private TextField newNameField;

    @FXML
    private ComboBox<String> membershipComboBox;

    @FXML
    private VBox parametersBox;

    @FXML
    private Label minValueLabel;

    @FXML
    private Label maxValueLabel;

    @FXML
    private ListView<String> summarizerValuesListView;



    @FXML
    public void initialize() {
        membershipComboBox.setOnAction(event -> generateParameterFields());

        // Główne korzenie TreeView
        TreeItem<String> rootItem = new TreeItem<>("Zmienna lingwistyczna");
        rootItem.setExpanded(true);

        // Dodaj zmienne lingwistyczne
        addLinguisticVariablesToTree(rootItem);

        // Ustaw TreeView
        linguisticTreeView.setRoot(rootItem);
        linguisticTreeView.setShowRoot(false); // Ukrywa główny węzeł "Zmienna lingwistyczna"
    }

    private void generateParameterFields() {
        // Wyczyszczenie poprzednich pól
        parametersBox.getChildren().clear();

        String selectedFunction = membershipComboBox.getValue();
        if ("Trójkątna".equals(selectedFunction)) {
            parametersBox.getChildren().add(createParameterField("a"));
            parametersBox.getChildren().add(createParameterField("b"));
            parametersBox.getChildren().add(createParameterField("c"));
        } else if ("Trapezowa".equals(selectedFunction)) {
            parametersBox.getChildren().add(createParameterField("a"));
            parametersBox.getChildren().add(createParameterField("b"));
            parametersBox.getChildren().add(createParameterField("c"));
            parametersBox.getChildren().add(createParameterField("d"));
        } else if ("Gaussowska".equals(selectedFunction)) {
            parametersBox.getChildren().add(createParameterField("μ"));
            parametersBox.getChildren().add(createParameterField("σ"));
        }
    }

    private HBox createParameterField(String parameterName) {
        Label label = new Label(parameterName + ":");
        TextField textField = new TextField();
        textField.setId("param-" + parameterName); // Ustawienie unikalnego ID dla każdego pola
        return new HBox(10, label, textField);
    }


    private void addLinguisticVariablesToTree(TreeItem<String> rootItem) {
        // Twórz węzły dla zmiennych lingwistycznych i ich sumaryzatorów
        addVariableWithSummarizers(rootItem, AirValues.airQuality, AirValues.bardzoDobra, AirValues.dobra, AirValues.umiarkowana, AirValues.zla, AirValues.bardzoZla);
        addVariableWithSummarizers(rootItem, HumidityValues.humidityVariable, HumidityValues.suche, HumidityValues.umiarkowane, HumidityValues.wilgotne);
        addVariableWithSummarizers(rootItem, TempValues.tempVariable, TempValues.bardzoZimna, TempValues.zimna, TempValues.umiarkowana, TempValues.ciepla, TempValues.goraca);
        addVariableWithSummarizers(rootItem, PressureValues.pressureVariable, PressureValues.niskie, PressureValues.normalne, PressureValues.wysokie);
        addVariableWithSummarizers(rootItem, WindValues.windVariable, WindValues.slaby, WindValues.umiarkowany, WindValues.silny, WindValues.bardzoSilny, WindValues.gwaltowny);
        addVariableWithSummarizers(rootItem, VisibilityValues.visibilityVariable, VisibilityValues.bardzoDobra, VisibilityValues.dobra, VisibilityValues.umiarkowana, VisibilityValues.slaba);
        addVariableWithSummarizers(rootItem, CoValues.coQualityVariable, CoValues.normalne, CoValues.wysokie, CoValues.niezdrowe, CoValues.niebezpieczne);
        addVariableWithSummarizers(rootItem, NoValues.no2QualityVariable, NoValues.normalne, NoValues.niezdrowe, NoValues.niebezpieczne);
        addVariableWithSummarizers(rootItem, UvValues.uvIndexVariable, UvValues.niskie, UvValues.umiarkowane, UvValues.wysokie, UvValues.bardzoWysokie, UvValues.ekstremalne);
        addVariableWithSummarizers(rootItem, TimeValues.timeVariable, TimeValues.poranna, TimeValues.poludniowa, TimeValues.popoludniowa, TimeValues.wieczorna, TimeValues.nocna);

        // Dodaj więcej zmiennych i ich sumaryzatorów w taki sam sposób
    }

    private void addVariableWithSummarizers(TreeItem<String> root, LinguisticVariable variable, LinguisticTerm... summarizers) {
        // Dodaj węzeł zmiennej lingwistycznej
        TreeItem<String> variableNode = new TreeItem<>(variable.getName());
        root.getChildren().add(variableNode);

        // Dodaj węzły sumaryzatorów jako dzieci tej zmiennej
        Arrays.stream(summarizers)
                .map(LinguisticTerm::getName)
                .map(TreeItem::new)
                .forEach(variableNode.getChildren()::add);
    }

    @FXML
    private void onTreeItemSelected() {
        TreeItem<String> selectedItem = linguisticTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {
            // Jeżeli wybrano zmienną lingwistyczną
            if (selectedItem.getParent().getParent() == null) {
                summarizerListView.getItems().clear();
                summarizerValuesListView.getItems().clear();

                // Pobierz nazwę zmiennej lingwistycznej
                String variableName = selectedItem.getValue();
                for (TreeItem<String> child : selectedItem.getChildren()) {
                    summarizerListView.getItems().add(child.getValue());
                }

                // Wyświetl minimalną i maksymalną wartość zmiennej
                LinguisticVariable variable = getLinguisticVariableByName(variableName);
                if (variable != null) {
                    minValueLabel.setText("Min: " + variable.getMin());
                    maxValueLabel.setText("Max: " + variable.getMax());
                }
            }
        }

    }

    @FXML
    private void onSaveSummarizer() {
        String lingusticVariable = linguisticTreeView.getSelectionModel().getSelectedItem().getValue();
        String newName = newNameField.getText();
        String selectedFunction = membershipComboBox.getValue();
        String selectedSummarizer = summarizerListView.getSelectionModel().getSelectedItem();

        if (selectedSummarizer == null || selectedFunction == null || newName.isEmpty()) {
            System.out.println("Uzupełnij wszystkie pola.");
            return;
        }

        // Pobierz wartości parametrów
        double[] params = parametersBox.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(hbox -> ((HBox) hbox).getChildren().get(1)) // Pobranie TextField
                .filter(node -> node instanceof TextField)
                .mapToDouble(node -> Double.parseDouble(((TextField) node).getText()))
                .toArray();

        // Aktualizacja sumaryzatora w modelu
        updateSummarizer(selectedSummarizer, newName, selectedFunction, params, lingusticVariable);
        System.out.println("Zapisano sumaryzator: " + newName + " z funkcją " + selectedFunction);
    }

    private void updateSummarizer(String originalName, String newName, String functionType, double[] params, String linguisticVariable) {
        LinguisticTerm summarizerToUpdate = findSummarizer(originalName, linguisticVariable);

        if (summarizerToUpdate != null) {
            // Aktualizacja nazwy
            summarizerToUpdate.setName(newName);

            // Aktualizacja funkcji przynależności
            switch (functionType) {
                case "Trójkątna":
                    summarizerToUpdate.setFuzzySet(new TriangularFunction(params[0], params[1], params[2]));
                    break;
                case "Trapezowa":
                    summarizerToUpdate.setFuzzySet(new TrapezoidalFunction(params[0], params[1], params[2], params[3]));
                    break;
                case "Gaussowska":
                    summarizerToUpdate.setFuzzySet(new GaussianFunction(params[0], params[1]));
                    break;
                default:
                    throw new IllegalArgumentException("Nieobsługiwany typ funkcji: " + functionType);
            }


            updateInVariablesClass(originalName, summarizerToUpdate, linguisticVariable);
            System.out.println("Zaktualizowano sumaryzator: " + originalName + " -> " + newName);


        } else {
            System.err.println("Sumaryzator " + originalName + " nie został znaleziony!");
        }

    }

    private LinguisticTerm findSummarizer(String nameSummarizer, String linguisticVariable) {
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

    private void updateInVariablesClass(String originalName, LinguisticTerm updatedTerm, String linguisticVariable) {
        // Aktualizacja w odpowiedniej klasie zmiennych lingwistycznych
        switch (linguisticVariable) {
            case "Jakość powietrza":
                if (originalName.equals(AirValues.bardzoDobra.getName())) {
                    AirValues.bardzoDobra = updatedTerm;
                } else if (originalName.equals(AirValues.dobra.getName())) {
                    AirValues.dobra = updatedTerm;
                } else if (originalName.equals(AirValues.umiarkowana.getName())) {
                    AirValues.umiarkowana = updatedTerm;
                } else if (originalName.equals(AirValues.zla.getName())) {
                    AirValues.zla = updatedTerm;
                } else if (originalName.equals(AirValues.bardzoZla.getName())) {
                    AirValues.bardzoZla = updatedTerm;
                }
                break;

            case "Ciśnienie":
                if (originalName.equals(PressureValues.niskie.getName())) {
                    PressureValues.niskie = updatedTerm;
                } else if (originalName.equals(PressureValues.normalne.getName())) {
                    PressureValues.normalne = updatedTerm;
                } else if (originalName.equals(PressureValues.wysokie.getName())) {
                    PressureValues.wysokie = updatedTerm;
                }
                break;
            case "Wilgotność powietrza":
                if (originalName.equals(HumidityValues.suche.getName())) {
                    HumidityValues.suche = updatedTerm;
                } else if (originalName.equals(HumidityValues.umiarkowane.getName())) {
                    HumidityValues.umiarkowane = updatedTerm;
                } else if (originalName.equals(HumidityValues.wilgotne.getName())) {
                    HumidityValues.wilgotne = updatedTerm;
                }
                break;

            case "Stopień widoczności":
                if (originalName.equals(VisibilityValues.slaba.getName())) {
                    VisibilityValues.slaba = updatedTerm;
                } else if (originalName.equals(VisibilityValues.umiarkowana.getName())) {
                    VisibilityValues.umiarkowana = updatedTerm;
                } else if (originalName.equals(VisibilityValues.dobra.getName())) {
                    VisibilityValues.dobra = updatedTerm;
                } else if (originalName.equals(VisibilityValues.bardzoDobra.getName())) {
                    VisibilityValues.bardzoDobra = updatedTerm;
                }
                break;

            case "Temperatura":
                if (originalName.equals(TempValues.bardzoZimna.getName())) {
                    TempValues.bardzoZimna = updatedTerm;
                } else if (originalName.equals(TempValues.zimna.getName())) {
                    TempValues.zimna = updatedTerm;
                } else if (originalName.equals(TempValues.umiarkowana.getName())) {
                    TempValues.umiarkowana = updatedTerm;
                } else if (originalName.equals(TempValues.ciepla.getName())) {
                    TempValues.ciepla = updatedTerm;
                } else if (originalName.equals(TempValues.goraca.getName())) {
                    TempValues.goraca = updatedTerm;
                }
                break;

            case "Wiatr":
                if (originalName.equals(WindValues.slaby.getName())) {
                    WindValues.slaby = updatedTerm;
                } else if (originalName.equals(WindValues.umiarkowany.getName())) {
                    WindValues.umiarkowany = updatedTerm;
                } else if (originalName.equals(WindValues.silny.getName())) {
                    WindValues.silny = updatedTerm;
                } else if (originalName.equals(WindValues.bardzoSilny.getName())) {
                    WindValues.bardzoSilny = updatedTerm;
                } else if (originalName.equals(WindValues.gwaltowny.getName())) {
                    WindValues.gwaltowny = updatedTerm;
                }
                break;

            case "Zanieczyszczenie CO":
                if (originalName.equals(CoValues.normalne.getName())) {
                    CoValues.normalne = updatedTerm;
                } else if (originalName.equals(CoValues.wysokie.getName())) {
                    CoValues.wysokie = updatedTerm;
                } else if (originalName.equals(CoValues.niezdrowe.getName())) {
                    CoValues.niezdrowe = updatedTerm;
                } else if (originalName.equals(CoValues.niebezpieczne.getName())) {
                    CoValues.niebezpieczne = updatedTerm;
                }
                break;

            case "Zanieczyszczenie NO2":
                if (originalName.equals(NoValues.normalne.getName())) {
                    NoValues.normalne = updatedTerm;
                } else if (originalName.equals(NoValues.niezdrowe.getName())) {
                    NoValues.niezdrowe = updatedTerm;
                } else if (originalName.equals(NoValues.niebezpieczne.getName())) {
                    NoValues.niebezpieczne = updatedTerm;
                }
                break;

            case "Pora dnia":
                if (originalName.equals(TimeValues.nocna.getName())) {
                    TimeValues.nocna = updatedTerm;
                } else if (originalName.equals(TimeValues.poranna.getName())) {
                    TimeValues.poranna = updatedTerm;
                } else if (originalName.equals(TimeValues.poludniowa.getName())) {
                    TimeValues.poludniowa = updatedTerm;
                } else if (originalName.equals(TimeValues.popoludniowa.getName())) {
                    TimeValues.popoludniowa = updatedTerm;
                } else if (originalName.equals(TimeValues.wieczorna.getName())) {
                    TimeValues.wieczorna = updatedTerm;
                }
                break;

            case "Promieniowanie UV":
                if (originalName.equals(UvValues.niskie.getName())) {
                    UvValues.niskie = updatedTerm;
                } else if (originalName.equals(UvValues.umiarkowane.getName())) {
                    UvValues.umiarkowane = updatedTerm;
                } else if (originalName.equals(UvValues.wysokie.getName())) {
                    UvValues.wysokie = updatedTerm;
                } else if (originalName.equals(UvValues.bardzoWysokie.getName())) {
                    UvValues.bardzoWysokie = updatedTerm;
                } else if (originalName.equals(UvValues.ekstremalne.getName())) {
                    UvValues.ekstremalne = updatedTerm;
                }
                break;

            default:
                System.err.println("Nieznana zmienna lingwistyczna: " + linguisticVariable);
                break;
        }

            // Możesz dodać więcej aktualizacji dla innych zmiennych lingwistycznych
    }

    @FXML
    private void onSummarizerSelected() {
        String selectedSummarizer = summarizerListView.getSelectionModel().getSelectedItem();
        String selectedVariable = linguisticTreeView.getSelectionModel().getSelectedItem().getValue();

        if (selectedSummarizer != null) {
            LinguisticTerm term = findSummarizer(selectedSummarizer, selectedVariable);
            summarizerValuesListView.getItems().clear();

            if (term != null) {
                summarizerValuesListView.getItems().add("Nazwa: " + term.getName());
                summarizerValuesListView.getItems().add("Typ funkcji: " + term.getFuzzySet().getClass().getSimpleName());

                // Wyciągnięcie parametrów funkcji przynależności
                FuzzySet fuzzySet = term.getFuzzySet();
                if (fuzzySet instanceof TrapezoidalFunction) {
                    TrapezoidalFunction trapezoidal = (TrapezoidalFunction) fuzzySet;
                    summarizerValuesListView.getItems().add("Wierzchołki: a=" + trapezoidal.getA() +
                            ", b=" + trapezoidal.getB() +
                            ", c=" + trapezoidal.getC() +
                            ", d=" + trapezoidal.getD());
                } else if (fuzzySet instanceof TriangularFunction) {
                    TriangularFunction triangular = (TriangularFunction) fuzzySet;
                    summarizerValuesListView.getItems().add("Wierzchołki: a=" + triangular.getA() +
                            ", b=" + triangular.getB() +
                            ", c=" + triangular.getC());
                } else if (fuzzySet instanceof GaussianFunction) {
                    GaussianFunction gaussian = (GaussianFunction) fuzzySet;
                    summarizerValuesListView.getItems().add("Parametry: center=" + gaussian.getCenter() +
                            ", σ=" + gaussian.getSigma());
                } else {
                    summarizerValuesListView.getItems().add("Nieobsługiwany typ funkcji przynależności");
                }
            }
        }

    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ksr/hello-view.fxml"));

            fxmlLoader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);

            Parent helloView = fxmlLoader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(helloView));
            stage.setTitle("Powrót do głównego widoku");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private LinguisticVariable getLinguisticVariableByName(String name) {
        // Dopasowanie do nazw zmiennych lingwistycznych
        if (name.equals(AirValues.airQuality.getName())) {
            return AirValues.airQuality;
        } else if (name.equals(PressureValues.pressureVariable.getName())) {
            return PressureValues.pressureVariable;
        } else if (name.equals(HumidityValues.humidityVariable.getName())) {
            return HumidityValues.humidityVariable;
        } else if (name.equals(VisibilityValues.visibilityVariable.getName())) {
            return VisibilityValues.visibilityVariable;
        } else if (name.equals(TempValues.tempVariable.getName())) {
            return TempValues.tempVariable;
        } else if (name.equals(WindValues.windVariable.getName())) {
            return WindValues.windVariable;
        } else if (name.equals(CoValues.coQualityVariable.getName())) {
            return CoValues.coQualityVariable;
        } else if (name.equals(NoValues.no2QualityVariable.getName())) {
            return NoValues.no2QualityVariable;
        } else if (name.equals(UvValues.uvIndexVariable.getName())) {
            return UvValues.uvIndexVariable;
        } else if (name.equals(TimeValues.timeVariable.getName())) {
            return TimeValues.timeVariable;
        }
        return null;
    }


}