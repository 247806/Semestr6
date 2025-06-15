package ksr.zad2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // Inicjalizacja Spring Boota
        springContext = new SpringApplicationBuilder(SpringJavaFxApp.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ksr/hello-view.fxml"));

        // Ustaw fabrykę kontrolerów – pobiera kontrolery z kontekstu Springa
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();

        primaryStage.setTitle("JavaFX + Spring Boot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}

