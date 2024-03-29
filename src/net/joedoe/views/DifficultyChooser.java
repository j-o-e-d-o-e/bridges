package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static net.joedoe.views.Difficulty.*;
import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class DifficultyChooser extends BorderPane {
    private EventHandler<DifficultyEvent> listener;

    DifficultyChooser(EventHandler<Event> listener) {
        getStylesheets().add("file:assets/css/default.css");
        ToolBar toolBar = new ToolBar("Time mode");
        toolBar.setListener(e -> listener.handle(null));
        setTop(toolBar);
        setCenter(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setId("border");

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Choose a difficulty:");
        title.setFont(Font.font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        Button veryEasy = new Button("Very easy (5 isles)");
        veryEasy.setPrefWidth(180);
        veryEasy.setOnAction(e -> listener.handle(new DifficultyEvent(VERY_EASY)));

        Button easy = new Button("Easy (25 isles)");
        easy.setPrefWidth(180);
        easy.setOnAction(e -> listener.handle(new DifficultyEvent(EASY)));

        Button medium = new Button("Medium (50 isles)");
        medium.setPrefWidth(180);
        medium.setOnAction(e -> listener.handle(new DifficultyEvent(MEDIUM)));

        Button hard = new Button("Hard (75 isles)");
        hard.setPrefWidth(180);
        hard.setOnAction(e -> listener.handle(new DifficultyEvent(HARD)));

        Button challenging = new Button("Challenging (100 isles)");
        challenging.setPrefWidth(180);
        challenging.setOnAction(e -> listener.handle(new DifficultyEvent(CHALLENGING)));

        box.getChildren().addAll(title, veryEasy, easy, medium, hard, challenging);
        innerPane.getChildren().addAll(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    void setNext(EventHandler<DifficultyEvent> listener) {
        this.listener = listener;
    }
}