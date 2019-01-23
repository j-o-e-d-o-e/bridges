package net.joedoe.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

class TopBar extends ToolBar {

    TopBar(SceneController controller, String goTo, String titleTxt) {
        Button back = null;
        if (!goTo.equals("")) {
            back = new Button("<");
            back.setPrefHeight(10);
            back.setOnAction(e -> controller.goTo(goTo));
            getItems().add(back);
        }
        Label title = new Label(titleTxt);
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: black");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        if (back != null) regionRight.setPrefWidth(regionLeft.getWidth() + 39);
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        getItems().addAll(regionLeft, title, regionRight);
    }

    TopBar(SceneController controller, String titleTxt) {
        this(controller, "", titleTxt);
    }
}
