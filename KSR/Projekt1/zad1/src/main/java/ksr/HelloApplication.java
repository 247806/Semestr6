package ksr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ksr/hello-view.fxml"));
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);

        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ksr/modena_dark.css")).toExternalForm());
        stage.setTitle("Zad 1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}