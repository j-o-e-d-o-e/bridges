package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.views.View.*;

class Start extends BorderPane {
    private Button load, resume;

    Start(EventHandler<ViewEvent> listener) {
        getStylesheets().add("file:assets/css/default.css");
        setTop(new ToolBar("Hashiwokakero", false));
        setCenter(setLayout(listener));
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout(EventHandler<ViewEvent> listener) {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        StackPane innerPane = new StackPane();
        innerPane.setId("border");

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Hashiwokakero");
        title.setFont(Font.font(40));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        Button newGame = new Button("New Game");
        newGame.setPrefWidth(100);
        newGame.setOnAction(e -> listener.handle(new ViewEvent(NEW)));
        resume = new Button("Resume");
        resume.setPrefWidth(100);
        resume.setOnAction(e -> listener.handle(new ViewEvent(RESUME)));
        load = new Button("Load Level");
        load.setPrefWidth(100);
        load.setOnAction(e -> listener.handle(new ViewEvent(LOAD)));
        Button bestScores = new Button("Best Scores");
        bestScores.setPrefWidth(100);
        bestScores.setOnAction(e -> listener.handle(new ViewEvent(BEST)));
        Button tutorial = new Button("How To");
        tutorial.setPrefWidth(100);
        tutorial.setOnAction(e -> listener.handle(new ViewEvent(HOWTO)));
        Button exit = new Button("Quit & Save");
        exit.setPrefWidth(100);
        exit.setOnAction(e -> listener.handle(new ViewEvent(QUIT)));
        box.getChildren().addAll(title, newGame, resume, load, bestScores, tutorial, exit);
        innerPane.getChildren().add(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    void disableResume(boolean disable) {
        resume.setDisable(disable);
    }

    void disableLoad(boolean disable) {
        load.setDisable(disable);
    }
}
