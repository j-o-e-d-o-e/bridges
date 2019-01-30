package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.joedoe.utils.GameData;
import net.joedoe.utils.PointEntry;
import net.joedoe.utils.TimeEntry;

import java.time.LocalTime;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class BestScores extends BorderPane {
    private GameData gameData = GameData.getInstance();

    BestScores(EventHandler<Event> listener) {
        getStylesheets().add("file:assets/css/bestscores.css");
        ToolBar toolBar = new ToolBar("Best scores");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);
        setCenter(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox vBox = new VBox(CONTAINER_OFFSET);
        vBox.setAlignment(Pos.CENTER);

        Label levelTitle = new Label("Level mode");
        levelTitle.setFont(new Font(20));

        HBox level = new HBox();
        level.setSpacing(CONTAINER_OFFSET);
        level.setPadding(new Insets(0, 0, 20, 0));
        level.setAlignment(Pos.CENTER);
        PointEntry bestLevel = gameData.getBestLevel();
        Label levelPoints = new Label(Integer.toString(bestLevel.getPoints()));
        Label levelName = new Label(bestLevel.getName());
        level.getChildren().addAll(levelPoints, new Label("-"), levelName);

        Label timeTitle = new Label("Time mode");
        timeTitle.setFont(new Font(20));
        vBox.getChildren().addAll(levelTitle, level, timeTitle);

        for (Difficulty difficulty : Difficulty.values()) {
            Label title = new Label(difficulty.getName() + " (" + difficulty.getIsles() + " isles)");
//            title.setStyle("-fx-font-weight: bold");
            title.setFont(new Font(14));

            HBox box = new HBox();
            box.setSpacing(CONTAINER_OFFSET);
            box.setPadding(new Insets(0, 0, 20, 0));
            box.setAlignment(Pos.CENTER);
            TimeEntry entry = gameData.getBestTime(difficulty);
            LocalTime time = entry.getTime();
            Label timeLbl = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
            Label name = new Label(entry.getName());
            box.getChildren().addAll(timeLbl, new Label("-"), name);
            vBox.getChildren().addAll(title, box);
        }
        innerPane.getChildren().add(vBox);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }
}