package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.joedoe.logics.Generator;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.views.ViewController.View.NEW;

class DifficultyChooser extends BorderPane {
    private ViewController controller;
    private Generator generator = new Generator();

    DifficultyChooser(ViewController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        ToolBar toolBar = new ToolBar("Time mode");
        toolBar.setListener(e -> controller.goTo(NEW));
        setTop(toolBar);
        setCenter(setLayout());
    }

    enum Difficulty {
        VERY_EASY(1), EASY(5), MEDIUM(10), HARD(15), CHALLENGING(20);
        private int level;

        Difficulty(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Choose a difficulty:");
        title.setFont(Font.font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));
        title.setStyle("-fx-text-fill: ghostwhite");

        Button veryEasy = new Button("Very easy (5 isles)");
        veryEasy.setPrefWidth(180);
        veryEasy.setOnAction(e -> handleInput(Difficulty.VERY_EASY));

        Button easy = new Button("Easy (25 isles)");
        easy.setPrefWidth(180);
        easy.setOnAction(e -> handleInput(Difficulty.EASY));

        Button medium = new Button("Medium (50 isles)");
        medium.setPrefWidth(180);
        medium.setOnAction(e -> handleInput(Difficulty.MEDIUM));

        Button hard = new Button("Hard (75 isles)");
        hard.setPrefWidth(180);
        hard.setOnAction(e -> handleInput(Difficulty.HARD));

        Button challenging = new Button("Challenging (100 isles)");
        challenging.setPrefWidth(180);
        challenging.setOnAction(e -> handleInput(Difficulty.CHALLENGING));

        box.getChildren().addAll(title, veryEasy, easy, medium, hard, challenging);
        innerPane.getChildren().addAll(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    private void handleInput(Difficulty difficulty) {
        generator.setData(difficulty.getLevel() * 5);
        generator.generateGame();
        controller.createBoard();
    }
}