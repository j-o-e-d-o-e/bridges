package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class Rules extends BorderPane {
    private SceneController controller;

    Rules(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setTop(new ToolBar(controller, "Start", "Rules"));
        setCenter(setLayout());
    }

    private Node setLayout() {
//        TableView

        Button ok = new Button("Ok");
        ok.setOnAction(e -> controller.createBoard());
        setCenter(ok);
        return ok;
    }
}
