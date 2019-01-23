package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.joedoe.utils.GameManager.Mode;

import java.util.Stack;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class ModeChooser extends BorderPane {
    private final SceneController controller;

    ModeChooser(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setTop(createToolbar());
        setCenter(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node createToolbar(){
        ToolBar bar = new ToolBar();
        Button back = new Button("<");
        back.setPrefHeight(10);
        back.setOnAction(e-> controller.goTo("Start"));
        Label title = new Label("New Game");
        title.setStyle("-fx-font-weight: bold");
        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);
        bar.getItems().addAll(back, regionLeft, title,regionRight);
        return bar;
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Choose a mode:");
        title.setFont(new Font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));
        title.setStyle("-fx-text-fill: ghostwhite");

        Button level = new Button("Level");
        level.setPrefWidth(100);
        level.setOnAction(e -> controller.createNewGame(Mode.LEVEL));
        Button time = new Button("Time");
        time.setPrefWidth(100);
        time.setOnAction(e -> controller.createNewGame(Mode.TIME));
        Button free = new Button("Free");
        free.setPrefWidth(100);
        free.setOnAction(e -> controller.createNewGame(Mode.FREE));
        box.getChildren().addAll(title, level, time, free);
        innerPane.getChildren().add(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }
}
