package net.joedoe.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

class TopBar extends ToolBar {

    TopBar(SceneController controller, String goTo, String titleTxt) {
        StackPane pane = new StackPane();
        if (!goTo.equals("")) {
            Button back = new Button("<");
            back.setPrefHeight(10);
            back.setOnAction(e -> controller.goTo(goTo));
            getItems().add(back);
        }
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        Label title = new Label(titleTxt);
        title.setStyle("-fx-font-weight: bold");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        getItems().addAll(regionLeft, title, regionRight);
    }

    TopBar(SceneController controller, String titleTxt) {
        this(controller, "", titleTxt);
    }
}
