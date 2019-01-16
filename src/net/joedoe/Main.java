package net.joedoe;

import javafx.application.Application;
import javafx.stage.Stage;
import net.joedoe.views.SceneController;

/**
 * Start-Klasse der Applikation.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hashiwokakero");
        SceneController controller = new SceneController(stage);
        stage.setOnCloseRequest(e -> {
            e.consume();
            controller.close();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
