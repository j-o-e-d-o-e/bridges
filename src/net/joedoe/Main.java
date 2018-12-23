package net.joedoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.joedoe.views.MainFrame;

import java.io.File;

/**
 * Start-Klasse der Applikation.
 */
public class Main extends Application {

    @Override
    public void start(Stage window) {
        window.setTitle("Joerg Doerwald / M-Nr. 6995349");
        MainFrame main = new MainFrame(window);
        Scene scene = new Scene(main, 768, 1024);
        scene.getStylesheets().add("file:assets" + File.separator + "css" + File.separator + "Dracula.css");
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
