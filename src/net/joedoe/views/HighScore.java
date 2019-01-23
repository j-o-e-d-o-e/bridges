package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class HighScore extends BorderPane {
    private SceneController controller;

    HighScore(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setTop(new TopBar(controller, "Start", "Highscore"));
        setCenter(setLayout());
    }

    private Node setLayout() {
//        TableView

        Button ok = new Button("Ok");
        ok.setOnAction(e -> controller.switchToBoard());
        setCenter(ok);
        return ok;
    }
}
