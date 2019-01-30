package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class ModeChooser extends BorderPane {
    private EventHandler<ModeEvent> listener;

    ModeChooser(EventHandler<Event> listener) {
        getStylesheets().add("file:assets/css/default.css");
        ToolBar toolBar = new ToolBar("New Game");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);

        setCenter(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setId("border");

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Choose a mode:");
        title.setFont(new Font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        Button level = new Button("Level");
        level.setPrefWidth(100);
        level.setOnAction(e -> listener.handle(new ModeEvent(Mode.LEVEL)));
        Button time = new Button("Time");
        time.setPrefWidth(100);
        time.setOnAction(e -> listener.handle(new ModeEvent(Mode.TIME)));
        Button free = new Button("Free");
        free.setPrefWidth(100);
        free.setOnAction(e -> listener.handle(new ModeEvent(Mode.FREE)));
        box.getChildren().addAll(title, level, time, free);
        innerPane.getChildren().add(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    void setNext(EventHandler<ModeEvent> listener) {
        this.listener = listener;
    }
}
