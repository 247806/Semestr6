package ksr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ksr.classifier.KNN;

import java.io.IOException;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField kTextField;

    @FXML
    private TextField proportionTextField;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        welcomeText.setText("Welcome to JavaFX Application!");
        //Main.main(null);
        int k= Integer.parseInt(kTextField.getText());
        double proportion= Double.parseDouble(proportionTextField.getText());
        KNN knn = new KNN(k, proportion);
    }
}