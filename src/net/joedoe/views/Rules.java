package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import static net.joedoe.views.ViewController.View.START;

class Rules extends BorderPane {
    private ViewController controller;

    Rules(ViewController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        ToolBar toolBar = new ToolBar("Rules");
        toolBar.setListener(e -> controller.goTo(START));
        setTop(toolBar);
        setCenter(setLayout());
    }

    private Node setLayout() {
//        TableView

        Button ok = new Button("Ok");
        ok.setOnAction(e -> controller.goTo(START));
        setCenter(ok);
        return ok;
    }
}
