package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

class Rules extends BorderPane {
    private SceneController controller;

    Rules(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setTop(createToolbar());
        setCenter(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node createToolbar() {
        ToolBar bar = new ToolBar();
        Button back = new Button("<");
        back.setPrefHeight(10);
        back.setOnAction(e -> controller.goTo("Start"));
        Label title = new Label("Rules");
        title.setStyle("-fx-font-weight: bold");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        bar.getItems().addAll(back, regionLeft, title, regionRight);
        return bar;
    }

    private Node setLayout() {
//        TableView

        Button ok = new Button("Ok");
        ok.setOnAction(e -> controller.switchToBoard());
        setCenter(ok);
        return ok;
    }
}
