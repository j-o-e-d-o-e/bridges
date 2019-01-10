package net.joedoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.joedoe.views.MainFrame;

/**
 * Start-Klasse der Applikation.
 */
public class Main extends Application {

    @Override
    public void start(Stage window) {
        window.setTitle("Hashiwokakero");
        MainFrame main = new MainFrame(window);
        Scene scene = new Scene(main, 768, 1024);
        scene.getStylesheets().add("file:assets/css/dracula.css");
        window.setScene(scene);
        window.setOnCloseRequest(e -> {
            e.consume();
            main.close();
        });
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
