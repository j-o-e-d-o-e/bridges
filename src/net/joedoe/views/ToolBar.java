package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ToolBar extends javafx.scene.control.ToolBar {
    private Button back;
    private Label title;

    ToolBar(String titleTxt, boolean backBtn) {
        if (backBtn) {
            back = new Button("<");
            back.setPrefHeight(10);
            back.setFocusTraversable(false);
            back.setMinWidth(40);
            getItems().add(this.back);
        }
        title = new Label(titleTxt);
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: black");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        if (backBtn) regionRight.setPrefWidth(regionLeft.getWidth() + back.getMinWidth());
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        getItems().addAll(regionLeft, title, regionRight);
    }

    public ToolBar(String title) {
        this(title, true);
    }

    public void updateTitle(String text) {
        title.setText(text);
    }

    public void setListener(EventHandler<Event> listener) {
        back.setOnAction(listener::handle);
    }
}
