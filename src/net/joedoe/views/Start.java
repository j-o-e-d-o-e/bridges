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
import net.joedoe.views.ViewController.View;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.views.ViewController.View.*;

class Start extends BorderPane {
    private Button load, resume;

    Start(EventHandler<ViewEvent> listener) {
        setStyle("-fx-background-color: #282828;");
        setTop(new ToolBar("Start", false));
        setCenter(setLayout(listener));
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout(EventHandler<ViewEvent> listener) {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Hashiwokakero");
        title.setFont(Font.font(40));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));
        title.setStyle("-fx-text-fill: ghostwhite");

        Button newGame = new Button("New Game");
        newGame.setPrefWidth(100);
        newGame.setOnAction(e -> listener.handle(new ViewEvent(NEW)));
        resume = new Button("Resume");
        resume.setPrefWidth(100);
        resume.setOnAction(e -> listener.handle(new ViewEvent(RESUME)));
        load = new Button("Load Level");
        load.setPrefWidth(100);
        load.setOnAction(e -> listener.handle(new ViewEvent(LOAD)));
        Button highScore = new Button("Highscore");
        highScore.setPrefWidth(100);
        highScore.setOnAction(e -> listener.handle(new ViewEvent(HIGHSCORE)));
        Button tutorial = new Button("Rules");
        tutorial.setPrefWidth(100);
        tutorial.setOnAction(e -> listener.handle(new ViewEvent(RULES)));
        Button exit = new Button("Quit & Save");
        exit.setPrefWidth(100);
        exit.setOnAction(e -> listener.handle(new ViewEvent(QUIT)));
        box.getChildren().addAll(title, newGame, resume, load, highScore, tutorial, exit);
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

    class ViewEvent extends Event {
        private View view;

        ViewEvent(View view) {
            super(null);
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }
}
