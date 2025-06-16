package ksr.zad2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//import ksr.classifier.KNN;
import javafx.stage.Stage;
import ksr.zad2.model.variables.*;
import ksr.zad2.repository.MeasurementsRepository;
import org.controlsfx.control.CheckListView;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HelloController {
    @FXML
    public ComboBox<String> quantificator;

    @FXML
    public ComboBox<String> data1;

    @FXML
    public ComboBox<String> sumariser1;

    @FXML
    public ComboBox<String> data2;

    @FXML
    public ComboBox<String> sumariser2;

    @FXML
    public ComboBox<String> sumariser3;

    @FXML
    public ComboBox<String> data3;

    @FXML
    public ComboBox<String> sumariser4;

    @FXML
    public ComboBox<String> data5;

    @FXML
    public ComboBox<String> qualificator;

    @FXML
    public ComboBox<String> data4;

    @FXML
    public ComboBox<String> sub1;

    @FXML
    public ComboBox<String> sub2;

    private QuantifierValues quantifierValues = new QuantifierValues();

    @FXML
    private void initialize() {
        quantificator.getItems().addAll(quantifierValues.Q1.getName(), quantifierValues.Q2.getName(), quantifierValues.Q3.getName(), quantifierValues.Q4.getName(), quantifierValues.Q5.getName());

        sub1.getItems().addAll("Ameryka", "Azja", "Europa", "Afryka", "Brak");
        sub1.setValue("Brak");
        sub2.setValue("Brak");

        sub1.setOnAction(event -> {
            String selected = sub1.getValue();
            sub2.getItems().clear();

            if (selected.equals("Ameryka")) {
                sub2.getItems().addAll("Azja", "Europa", "Afryka");
            } else if (selected.equals("Azja")) {
                sub2.getItems().addAll("Ameryka", "Europa", "Afryka");
            } else if (selected.equals("Europa")) {
                sub2.getItems().addAll("Azja", "Ameryka", "Afryka");
            } else if (selected.equals("Afryka")) {
                sub2.getItems().addAll("Azja", "Europa", "Ameryka");
            } else if (selected.equals("Brak")) {
                sub2.getItems().addAll("Brak");
                sub2.setValue("Brak");
            }
        });


        data1.getItems().addAll(AirValues.airQuality.getName(), CoValues.coQualityVariable.getName(),
                HumidityValues.humidityVariable.getName(), NoValues.no2QualityVariable.getName(), PressureValues.pressureVariable.getName(),
                TempValues.tempVariable.getName(), WindValues.windVariable.getName(), TimeValues.timeVariable.getName(),
                UvValues.uvIndexVariable.getName(), VisibilityValues.visibilityVariable.getName());

        data1.setOnAction(event -> {
            String selected = data1.getValue();
            sumariser1.getItems().clear();

            if (AirValues.airQuality.getName().equals(selected)) {
                sumariser1.getItems().addAll(AirValues.zla.getName(), AirValues.bardzoZla.getName(), AirValues.dobra.getName(), AirValues.bardzoDobra.getName(), AirValues.umiarkowana.getName());
            } else if (CoValues.coQualityVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(CoValues.niebezpieczne.getName(), CoValues.niezdrowe.getName(), CoValues.normalne.getName(), CoValues.wysokie.getName());
            } else if (HumidityValues.humidityVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(HumidityValues.umiarkowane.getName(), HumidityValues.wilgotne.getName(), HumidityValues.suche.getName());
            } else if (NoValues.no2QualityVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(NoValues.niebezpieczne.getName(), NoValues.normalne.getName(), NoValues.niezdrowe.getName());
            } else if (PressureValues.pressureVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(PressureValues.normalne.getName(), PressureValues.niskie.getName(), PressureValues.wysokie.getName());
            } else if (TempValues.tempVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(TempValues.bardzoZimna.getName(), TempValues.zimna.getName(), TempValues.umiarkowana.getName(), TempValues.ciepla.getName(), TempValues.goraca.getName());
            } else if (WindValues.windVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(WindValues.bardzoSilny.getName(), WindValues.silny.getName(), WindValues.gwaltowny.getName(), WindValues.slaby.getName(), WindValues.umiarkowany.getName());
            } else if (TimeValues.timeVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(TimeValues.nocna.getName(), TimeValues.poludniowa.getName(), TimeValues.popoludniowa.getName(), TimeValues.poranna.getName(), TimeValues.wieczorna.getName());
            } else if (UvValues.uvIndexVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(UvValues.wysokie.getName(), UvValues.umiarkowane.getName(), UvValues.ekstremalne.getName(), UvValues.bardzoWysokie.getName(), UvValues.niskie.getName());
            } else if (VisibilityValues.visibilityVariable.getName().equals(selected)) {
                sumariser1.getItems().addAll(VisibilityValues.bardzoDobra.getName(), VisibilityValues.dobra.getName(), VisibilityValues.umiarkowana.getName(), VisibilityValues.slaba.getName());
            }
        });

        data2.getItems().addAll(AirValues.airQuality.getName(), CoValues.coQualityVariable.getName(),
                HumidityValues.humidityVariable.getName(), NoValues.no2QualityVariable.getName(), PressureValues.pressureVariable.getName(),
                TempValues.tempVariable.getName(), WindValues.windVariable.getName(), TimeValues.timeVariable.getName(),
                UvValues.uvIndexVariable.getName(), VisibilityValues.visibilityVariable.getName(), "Brak");

        data2.setValue("Brak");
        sumariser2.setValue("Brak");

        data2.setOnAction(event -> {
            String selected = data2.getValue();
            sumariser2.getItems().clear();

            if (AirValues.airQuality.getName().equals(selected)) {
                sumariser2.getItems().addAll(AirValues.zla.getName(), AirValues.bardzoZla.getName(), AirValues.dobra.getName(), AirValues.bardzoDobra.getName(), AirValues.umiarkowana.getName());
            } else if (CoValues.coQualityVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(CoValues.niebezpieczne.getName(), CoValues.niezdrowe.getName(), CoValues.normalne.getName(), CoValues.wysokie.getName());
            } else if (HumidityValues.humidityVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(HumidityValues.umiarkowane.getName(), HumidityValues.wilgotne.getName(), HumidityValues.suche.getName());
            } else if (NoValues.no2QualityVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(NoValues.niebezpieczne.getName(), NoValues.normalne.getName(), NoValues.niezdrowe.getName());
            } else if (PressureValues.pressureVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(PressureValues.normalne.getName(), PressureValues.niskie.getName(), PressureValues.wysokie.getName());
            } else if (TempValues.tempVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(TempValues.bardzoZimna.getName(), TempValues.zimna.getName(), TempValues.umiarkowana.getName(), TempValues.ciepla.getName(), TempValues.goraca.getName());
            } else if (WindValues.windVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(WindValues.bardzoSilny.getName(), WindValues.silny.getName(), WindValues.gwaltowny.getName(), WindValues.slaby.getName(), WindValues.umiarkowany.getName());
            } else if (TimeValues.timeVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(TimeValues.nocna.getName(), TimeValues.poludniowa.getName(), TimeValues.popoludniowa.getName(), TimeValues.poranna.getName(), TimeValues.wieczorna.getName());
            } else if (UvValues.uvIndexVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(UvValues.wysokie.getName(), UvValues.umiarkowane.getName(), UvValues.ekstremalne.getName(), UvValues.bardzoWysokie.getName(), UvValues.niskie.getName());
            } else if (VisibilityValues.visibilityVariable.getName().equals(selected)) {
                sumariser2.getItems().addAll(VisibilityValues.bardzoDobra.getName(), VisibilityValues.dobra.getName(), VisibilityValues.umiarkowana.getName(), VisibilityValues.slaba.getName());
            } else if ("Brak".equals(selected)) {
                sumariser2.getItems().addAll("Brak");
                sumariser2.setValue("Brak");
            }
        });

        data3.getItems().addAll(AirValues.airQuality.getName(), CoValues.coQualityVariable.getName(),
                HumidityValues.humidityVariable.getName(), NoValues.no2QualityVariable.getName(), PressureValues.pressureVariable.getName(),
                TempValues.tempVariable.getName(), WindValues.windVariable.getName(), TimeValues.timeVariable.getName(),
                UvValues.uvIndexVariable.getName(), VisibilityValues.visibilityVariable.getName(), "Brak");
        data3.setValue("Brak");
        sumariser3.setValue("Brak");
        data3.setOnAction(event -> {
            String selected = data3.getValue();
            sumariser3.getItems().clear();

            if (AirValues.airQuality.getName().equals(selected)) {
                sumariser3.getItems().addAll(AirValues.zla.getName(), AirValues.bardzoZla.getName(), AirValues.dobra.getName(), AirValues.bardzoDobra.getName(), AirValues.umiarkowana.getName());
            } else if (CoValues.coQualityVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(CoValues.niebezpieczne.getName(), CoValues.niezdrowe.getName(), CoValues.normalne.getName(), CoValues.wysokie.getName());
            } else if (HumidityValues.humidityVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(HumidityValues.umiarkowane.getName(), HumidityValues.wilgotne.getName(), HumidityValues.suche.getName());
            } else if (NoValues.no2QualityVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(NoValues.niebezpieczne.getName(), NoValues.normalne.getName(), NoValues.niezdrowe.getName());
            } else if (PressureValues.pressureVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(PressureValues.normalne.getName(), PressureValues.niskie.getName(), PressureValues.wysokie.getName());
            } else if (TempValues.tempVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(TempValues.bardzoZimna.getName(), TempValues.zimna.getName(), TempValues.umiarkowana.getName(), TempValues.ciepla.getName(), TempValues.goraca.getName());
            } else if (WindValues.windVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(WindValues.bardzoSilny.getName(), WindValues.silny.getName(), WindValues.gwaltowny.getName(), WindValues.slaby.getName(), WindValues.umiarkowany.getName());
            } else if (TimeValues.timeVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(TimeValues.nocna.getName(), TimeValues.poludniowa.getName(), TimeValues.popoludniowa.getName(), TimeValues.poranna.getName(), TimeValues.wieczorna.getName());
            } else if (UvValues.uvIndexVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(UvValues.wysokie.getName(), UvValues.umiarkowane.getName(), UvValues.ekstremalne.getName(), UvValues.bardzoWysokie.getName(), UvValues.niskie.getName());
            } else if (VisibilityValues.visibilityVariable.getName().equals(selected)) {
                sumariser3.getItems().addAll(VisibilityValues.bardzoDobra.getName(), VisibilityValues.dobra.getName(), VisibilityValues.umiarkowana.getName(), VisibilityValues.slaba.getName());
            } else if ("Brak".equals(selected)) {
                sumariser3.getItems().addAll("Brak");
                sumariser3.setValue("Brak");
            }
        });

        data4.getItems().addAll(AirValues.airQuality.getName(), CoValues.coQualityVariable.getName(),
                HumidityValues.humidityVariable.getName(), NoValues.no2QualityVariable.getName(), PressureValues.pressureVariable.getName(),
                TempValues.tempVariable.getName(), WindValues.windVariable.getName(), TimeValues.timeVariable.getName(),
                UvValues.uvIndexVariable.getName(), VisibilityValues.visibilityVariable.getName(), "Brak");
        data4.setValue("Brak");
        sumariser4.setValue("Brak");
        data4.setOnAction(event -> {
            String selected = data4.getValue();
            sumariser4.getItems().clear();

            if (AirValues.airQuality.getName().equals(selected)) {
                sumariser4.getItems().addAll(AirValues.zla.getName(), AirValues.bardzoZla.getName(), AirValues.dobra.getName(), AirValues.bardzoDobra.getName(), AirValues.umiarkowana.getName());
            } else if (CoValues.coQualityVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(CoValues.niebezpieczne.getName(), CoValues.niezdrowe.getName(), CoValues.normalne.getName(), CoValues.wysokie.getName());
            } else if (HumidityValues.humidityVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(HumidityValues.umiarkowane.getName(), HumidityValues.wilgotne.getName(), HumidityValues.suche.getName());
            } else if (NoValues.no2QualityVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(NoValues.niebezpieczne.getName(), NoValues.normalne.getName(), NoValues.niezdrowe.getName());
            } else if (PressureValues.pressureVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(PressureValues.normalne.getName(), PressureValues.niskie.getName(), PressureValues.wysokie.getName());
            } else if (TempValues.tempVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(TempValues.bardzoZimna.getName(), TempValues.zimna.getName(), TempValues.umiarkowana.getName(), TempValues.ciepla.getName(), TempValues.goraca.getName());
            } else if (WindValues.windVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(WindValues.bardzoSilny.getName(), WindValues.silny.getName(), WindValues.gwaltowny.getName(), WindValues.slaby.getName(), WindValues.umiarkowany.getName());
            } else if (TimeValues.timeVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(TimeValues.nocna.getName(), TimeValues.poludniowa.getName(), TimeValues.popoludniowa.getName(), TimeValues.poranna.getName(), TimeValues.wieczorna.getName());
            } else if (UvValues.uvIndexVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(UvValues.wysokie.getName(), UvValues.umiarkowane.getName(), UvValues.ekstremalne.getName(), UvValues.bardzoWysokie.getName(), UvValues.niskie.getName());
            } else if (VisibilityValues.visibilityVariable.getName().equals(selected)) {
                sumariser4.getItems().addAll(VisibilityValues.bardzoDobra.getName(), VisibilityValues.dobra.getName(), VisibilityValues.umiarkowana.getName(), VisibilityValues.slaba.getName());
            } else if ("Brak".equals(selected)) {
                sumariser4.getItems().addAll("Brak");
                sumariser4.setValue("Brak");
            }
        });

        data5.getItems().addAll(AirValues.airQuality.getName(), CoValues.coQualityVariable.getName(),
                HumidityValues.humidityVariable.getName(), NoValues.no2QualityVariable.getName(), PressureValues.pressureVariable.getName(),
                TempValues.tempVariable.getName(), WindValues.windVariable.getName(), TimeValues.timeVariable.getName(),
                UvValues.uvIndexVariable.getName(), VisibilityValues.visibilityVariable.getName(), "Brak");
        data5.setValue("Brak");
        qualificator.setValue("Brak");

        data5.setOnAction(event -> {
            String selected = data5.getValue();
            qualificator.getItems().clear();

            if (AirValues.airQuality.getName().equals(selected)) {
                qualificator.getItems().addAll(AirValues.zla.getName(), AirValues.bardzoZla.getName(), AirValues.dobra.getName(), AirValues.bardzoDobra.getName(), AirValues.umiarkowana.getName());
            } else if (CoValues.coQualityVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(CoValues.niebezpieczne.getName(), CoValues.niezdrowe.getName(), CoValues.normalne.getName(), CoValues.wysokie.getName());
            } else if (HumidityValues.humidityVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(HumidityValues.umiarkowane.getName(), HumidityValues.wilgotne.getName(), HumidityValues.suche.getName());
            } else if (NoValues.no2QualityVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(NoValues.niebezpieczne.getName(), NoValues.normalne.getName(), NoValues.niezdrowe.getName());
            } else if (PressureValues.pressureVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(PressureValues.normalne.getName(), PressureValues.niskie.getName(), PressureValues.wysokie.getName());
            } else if (TempValues.tempVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(TempValues.bardzoZimna.getName(), TempValues.zimna.getName(), TempValues.umiarkowana.getName(), TempValues.ciepla.getName(), TempValues.goraca.getName());
            } else if (WindValues.windVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(WindValues.bardzoSilny.getName(), WindValues.silny.getName(), WindValues.gwaltowny.getName(), WindValues.slaby.getName(), WindValues.umiarkowany.getName());
            } else if (TimeValues.timeVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(TimeValues.nocna.getName(), TimeValues.poludniowa.getName(), TimeValues.popoludniowa.getName(), TimeValues.poranna.getName(), TimeValues.wieczorna.getName());
            } else if (UvValues.uvIndexVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(UvValues.wysokie.getName(), UvValues.umiarkowane.getName(), UvValues.ekstremalne.getName(), UvValues.bardzoWysokie.getName(), UvValues.niskie.getName());
            } else if (VisibilityValues.visibilityVariable.getName().equals(selected)) {
                qualificator.getItems().addAll(VisibilityValues.bardzoDobra.getName(), VisibilityValues.dobra.getName(), VisibilityValues.umiarkowana.getName(), VisibilityValues.slaba.getName());
            } else if ("Brak".equals(selected)) {
                qualificator.getItems().addAll("Brak");
                qualificator.setValue("Brak");
            }
        });

    }

    @FXML
    protected void onHelloButtonClick() throws IOException {
        System.out.println(sub1.getValue());
        System.out.println(sub2.getValue());
        if (sub1.getValue().equals("Brak") && sub2.getValue().equals("Brak")) {
            KsrApplication.singleSubjectSummary(data1.getValue(), data2.getValue(), data3.getValue(), data4.getValue(), data5.getValue(), sumariser1.getValue(), sumariser2.getValue(), sumariser3.getValue(), sumariser4.getValue(), qualificator.getValue(), quantificator.getValue());
        } else {
            KsrApplication.twoSubjectSummary();
        }

    }

    @FXML
    public void onAdminButtonClick(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ksr/admin-view.fxml"));

            // Pobierz kontrolery z kontekstu Springa
            fxmlLoader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);

            // Załaduj widok
            Parent adminView = fxmlLoader.load();

            // Pobierz bieżącą scenę i zmień ją na nową
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(adminView));
            stage.setTitle("Panel Admina");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}