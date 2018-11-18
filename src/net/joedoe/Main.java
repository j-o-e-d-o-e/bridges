package net.joedoe;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.joedoe.views.MainFrame;

public class Main extends Application {

    @Override
    public void start(Stage window) {
        window.setTitle("Joerg Doerwald / M-Nr. 6995349");
        MainFrame main = new MainFrame(window);
        Scene scene = new Scene(main, 768, 1024);
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
