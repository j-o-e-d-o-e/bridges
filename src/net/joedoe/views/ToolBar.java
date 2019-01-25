package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

class ToolBar extends javafx.scene.control.ToolBar {
    private Button back;
    private Label title;

    ToolBar(String title, boolean back) {
        this.back = null;
        if (back) {
            this.back = new Button("<");
            this.back.setPrefHeight(10);
            this.back.setMinWidth(40);
            getItems().add(this.back);
        }
        this.title = new Label(title);
        this.title.setStyle("-fx-font-weight: bold; -fx-text-fill: black");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        if (back) regionRight.setPrefWidth(regionLeft.getWidth() + this.back.getMinWidth());
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        getItems().addAll(regionLeft, this.title, regionRight);
    }

    ToolBar(String title) {
        this(title, true);
    }

    void updateTitle(String text) {
        title.setText(text);
    }

    void setListener(EventHandler<Event> listener) {
        back.setOnAction(listener::handle);
    }
}
