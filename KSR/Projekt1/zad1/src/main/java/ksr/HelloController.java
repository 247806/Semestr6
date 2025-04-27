package ksr;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import ksr.classifier.KNN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;
import org.controlsfx.control.CheckListView;

public class HelloController {
    @FXML
    public ComboBox<String> metric;

    @FXML
    public ComboBox<String> country;

    @FXML
    private TextField kTextField;

    @FXML
    private TextField proportionTextField;

    @FXML
    private Label accuracy;

    @FXML
    private Label precision;

    @FXML
    private Label recall;

    @FXML
    private Label f1Score;

    @FXML
    private Label error;

    @FXML
    private CheckListView<String> checkListView;

    private KNN knn;


    @FXML
    private void initialize() {
        metric.getItems().addAll("Euklidesowa", "Uliczna", "Czebyszewa");
        country.getItems().addAll("USA", "Canada", "United Kingdom", "France", "West-Germany", "Japan");
        checkListView.getItems().addAll("Długość tekstu", "Dominująca waluta", "Nazwy miejsca", "Liczba unikalnych słów",
                "Średnia długość słowa", "Liczba słów kluczowych w pierwszych 3 zdaniach", "Liczba słów kluczowych zaczynających się wielką literą",
                "Pierwsze słowo kluczowe w tekście", "Liczba słów kluczowych", "Względna liczba słów kluczowych", "Nazwiska");

        country.setOnAction(event -> {
            String selectedCountry = country.getValue();
//            System.out.println("Wybrano kraj: " + selectedCountry);

            int countryType = switch (selectedCountry) {
                case "USA" -> 0;
                case "United Kingdom" -> 1;
                case "Canada" -> 2;
                case "France" -> 3;
                case "West-Germany" -> 4;
                case "Japan" -> 5;
                default -> throw new IllegalArgumentException("Unsupported country: " + selectedCountry);
            };

            if (knn != null) {
                double prec;
                double rec;
                double f1;

                if ( knn.getQualityMeasure()[countryType][0].isNaN()) {
                    prec = 0;
                } else {
                    prec = knn.getQualityMeasure()[countryType][0];
                }

                if ( knn.getQualityMeasure()[countryType][1].isNaN()) {
                    rec = 0;
                } else {
                    rec = knn.getQualityMeasure()[countryType][1];
                }

                if ( knn.getQualityMeasure()[countryType][2].isNaN()) {
                    f1 = 0;
                } else {
                    f1 = knn.getQualityMeasure()[countryType][2];
                }

                precision.setText(String.format("Precision: %.2f%%", prec * 100));
                recall.setText(String.format("Recall: %.2f%%", rec * 100));
                f1Score.setText(String.format("F1 Score: %.2f%%", f1 * 100));
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() throws IOException {
        try {
            // Sprawdzenie wartości k
            int k = Integer.parseInt(kTextField.getText());
            if (k <= 0) {
                throw new IllegalArgumentException("Wartość K musi być liczbą dodatnią.");
            }

            // Sprawdzenie wartości proportion
            double proportion = Double.parseDouble(proportionTextField.getText());
            if (proportion <= 0 || proportion > 1) {
                throw new IllegalArgumentException("Proporcja musi być liczbą z zakresu (0, 1].");
            }

            // Sprawdzenie, czy wybrano metrykę
            String selectedMetric = metric.getValue();
            if (selectedMetric == null || selectedMetric.isEmpty()) {
                throw new IllegalArgumentException("Musisz wybrać metrykę.");
            }

            knn = new KNN(k, proportion, selectedMetric, getSelectedOptions());

            accuracy.setText(String.format("Accuracy: %.2f%%", knn.getAccuracy() * 100));

        } catch (NumberFormatException e) {
            error.setText("Błąd: Wprowadź poprawne liczby dla K i proporcji.");
        } catch (IllegalArgumentException e) {
            error.setText("Błąd: " + e.getMessage());
        }

    }

    @FXML
    protected void onResetButtonClick() {
        File file = new File("allArticles.json");
        if (file.exists()) {
            file.delete();
        } else {
            error.setText("Wartości jeszcze nie zostały ustalone.");
        }
    }

    public List<Integer> getSelectedOptions() {
        return new ArrayList<>(checkListView.getCheckModel().getCheckedIndices());
    }

}