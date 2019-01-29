package net.joedoe;

import javafx.application.Application;
import javafx.stage.Stage;
import net.joedoe.views.ViewController;

/**
 * Start-Klasse der Applikation.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hashiwokakero");
        stage.setWidth(768);
        stage.setHeight(1024);
        ViewController controller = new ViewController(stage);
        stage.setOnCloseRequest(e -> {
            e.consume();
            controller.close();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
