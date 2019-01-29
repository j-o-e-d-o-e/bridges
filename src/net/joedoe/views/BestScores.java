package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.joedoe.utils.GameData;
import net.joedoe.utils.PointEntry;
import net.joedoe.utils.TimeEntry;

import java.time.LocalTime;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.views.Difficulty.*;

class BestScores extends BorderPane {
    private GameData gameData = GameData.getInstance();
    private Button levelBtn, veryEasyBtn, easyBtn, mediumBtn, hardBtn, challengingBtn;
    private Label levelPoints, veryEasyTime, easyTime, mediumTime, hardTime, challengingTime;
    private TextField levelName, veryEasyName, easyName, mediumName, hardName, challengingName;
    private EventHandler<Event> listener;

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


        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label levelTitle = new Label("Level mode:");
        levelTitle.setFont(new Font(20));
        levelTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox levelBox = new HBox();
        levelBox.setAlignment(Pos.CENTER);
        PointEntry bestLevel = gameData.getBestLevel();
        levelPoints = new Label(Integer.toString(bestLevel.getPoints()));
        levelName = new TextField(bestLevel.getName());
        levelName.setEditable(false);
        levelName.setFocusTraversable(false);
        levelBtn = new Button("Ok");
        levelBtn.setVisible(false);
        levelBtn.setOnAction(e -> handleInput());
        levelBox.getChildren().addAll(levelPoints, new Label("   "), levelName, levelBtn);

        Label timeTitle = new Label("Time mode:");
        timeTitle.setFont(new Font(20));
        timeTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        Label veryEasyTitle = new Label("Very easy:");
        veryEasyTitle.setFont(new Font(14));
        veryEasyTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox timeVeryEasy = new HBox();
        timeVeryEasy.setAlignment(Pos.CENTER);
        TimeEntry bestVeryEasy = gameData.getBestVeryEasy();
        LocalTime time = bestVeryEasy.getTime();
        veryEasyTime = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
        veryEasyName = new TextField(bestVeryEasy.getName());
        veryEasyName.setEditable(false);
        veryEasyName.setFocusTraversable(false);
        veryEasyBtn = new Button("Ok");
        veryEasyBtn.setVisible(false);
        veryEasyBtn.setOnAction(e -> handleInput(VERY_EASY));
        timeVeryEasy.getChildren().addAll(veryEasyTime, new Label("   "), veryEasyName, veryEasyBtn);

        Label easyTitle = new Label("Easy:");
        easyTitle.setFont(new Font(14));
        easyTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox timeEasy = new HBox();
        timeEasy.setAlignment(Pos.CENTER);
        TimeEntry bestEasy = gameData.getBestEasy();
        time = bestEasy.getTime();
        easyTime = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
        easyName = new TextField(bestEasy.getName());
        easyName.setEditable(false);
        easyName.setFocusTraversable(false);
        easyBtn = new Button("Ok");
        easyBtn.setVisible(false);
        easyBtn.setOnAction(e -> handleInput(EASY));
        timeEasy.getChildren().addAll(easyTime, new Label("   "), easyName, easyBtn);

        Label mediumTitle = new Label("Medium:");
        mediumTitle.setFont(new Font(14));
        mediumTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox timeMedium = new HBox();
        timeMedium.setAlignment(Pos.CENTER);
        TimeEntry bestMedium = gameData.getBestMedium();
        time = bestMedium.getTime();
        mediumTime = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
        mediumName = new TextField(bestMedium.getName());
        mediumName.setEditable(false);
        mediumName.setFocusTraversable(false);
        mediumBtn = new Button("Ok");
        mediumBtn.setVisible(false);
        mediumBtn.setOnAction(e -> handleInput(MEDIUM));
        timeMedium.getChildren().addAll(mediumTime, new Label("   "), mediumName, mediumBtn);

        Label hardTitle = new Label("Hard:");
        hardTitle.setFont(new Font(14));
        hardTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox timeHard = new HBox();
        timeHard.setAlignment(Pos.CENTER);
        TimeEntry bestHard = gameData.getBestHard();
        time = bestHard.getTime();
        hardTime = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
        hardName = new TextField(bestHard.getName());
        hardName.setEditable(false);
        hardName.setFocusTraversable(false);
        hardBtn = new Button("Ok");
        hardBtn.setVisible(false);
        hardBtn.setOnAction(e -> handleInput(HARD));
        timeHard.getChildren().addAll(hardTime, new Label("   "), hardName, hardBtn);

        Label challengingTitle = new Label("Challenging:");
        challengingTitle.setFont(new Font(14));
        challengingTitle.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox timeChallenging = new HBox();
        timeChallenging.setAlignment(Pos.CENTER);
        TimeEntry bestChallenging = gameData.getBestChallenging();
        time = bestChallenging.getTime();
        challengingTime = new Label(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
        challengingName = new TextField(bestChallenging.getName());
        challengingName.setEditable(false);
        challengingName.setFocusTraversable(false);
        challengingBtn = new Button("Ok");
        challengingBtn.setVisible(false);
        challengingBtn.setOnAction(e -> handleInput(HARD));
        timeChallenging.getChildren().addAll(challengingTime, new Label("   "), challengingName, challengingBtn);

        box.getChildren().addAll(levelTitle, levelBox, timeTitle, veryEasyTitle, timeVeryEasy, easyTitle, timeEasy, mediumTitle, timeMedium, hardTitle, timeHard, challengingTitle, timeChallenging);
        innerPane.getChildren().add(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    private void handleInput(Difficulty difficulty) {
        TimeEntry entry;
        switch (difficulty) {
            case VERY_EASY:
                entry = gameData.getBestVeryEasy();
                entry.setName(veryEasyName.getText());
                veryEasyName.setEditable(false);
                veryEasyName.setFocusTraversable(false);
                veryEasyBtn.setVisible(false);
                break;
            case EASY:
                entry = gameData.getBestEasy();
                entry.setName(easyName.getText());
                easyName.setEditable(false);
                easyName.setFocusTraversable(false);
                easyBtn.setVisible(false);
                break;
            case MEDIUM:
                entry = gameData.getBestMedium();
                entry.setName(mediumName.getText());
                mediumName.setEditable(false);
                mediumName.setFocusTraversable(false);
                mediumBtn.setVisible(false);
                break;
            case HARD:
                entry = gameData.getBestHard();
                entry.setName(hardName.getText());
                hardName.setEditable(false);
                hardName.setFocusTraversable(false);
                hardBtn.setVisible(false);
                break;
            case CHALLENGING:
                entry = gameData.getBestChallenging();
                entry.setName(challengingName.getText());
                challengingName.setEditable(false);
                challengingName.setFocusTraversable(false);
                challengingBtn.setVisible(false);
                break;
        }
        listener.handle(new Event(null));
    }

    private void handleInput() {
        PointEntry pointEntry = gameData.getBestLevel();
        pointEntry.setName(levelName.getText());
        levelName.setEditable(false);
        levelName.setFocusTraversable(false);
        levelBtn.setVisible(false);
        listener.handle(new Event(null));
    }

    void setLevelPoints(int points) {
        levelPoints.setText(Integer.toString(points));
    }

    void setVeryEasyTime(LocalTime time) {
        veryEasyTime.setText(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
    }

    void setEasyTime(LocalTime time) {
        easyTime.setText(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
    }

    void setMediumTime(LocalTime time) {
        mediumTime.setText(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
    }

    void setHardTime(LocalTime time) {
        hardTime.setText(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
    }

    void setChallengingTime(LocalTime time) {
        challengingTime.setText(String.format("%02d:%02d", time.getMinute(), time.getSecond()));
    }

    void setEditableLevel() {
        levelName.setEditable(true);
        levelName.setFocusTraversable(true);
        levelBtn.setVisible(true);
    }

    void setEditableVeryEasy() {
        veryEasyName.setEditable(true);
        veryEasyName.setFocusTraversable(true);
        veryEasyBtn.setVisible(true);
    }

    void setEditableEasy() {
        easyName.setEditable(true);
        easyName.setFocusTraversable(true);
        easyBtn.setVisible(true);
    }

    void setEditableMedium() {
        mediumName.setEditable(true);
        mediumName.setFocusTraversable(true);
        mediumBtn.setVisible(true);
    }

    void setEditableHard() {
        hardName.setEditable(true);
        hardName.setFocusTraversable(true);
        hardBtn.setVisible(true);
    }

    void setEditableChallenging() {
        challengingName.setEditable(true);
        challengingName.setFocusTraversable(true);
        challengingBtn.setVisible(true);
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }
}