package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.logics.Generator;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class DifficultyChooser extends StackPane {
    private final SceneController controller;
    private Generator generator = new Generator();
    private ToggleGroup group;

    DifficultyChooser(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        getChildren().add(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane stackPane = new StackPane();
        stackPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        stackPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        VBox vBox = new VBox(CONTAINER_OFFSET);
        group = new ToggleGroup();

        RadioButton veryEasy = new RadioButton("Very easy (5 isles)");
        veryEasy.setUserData(Difficulty.VERY_EASY);
        veryEasy.setStyle("-fx-text-fill: #F8F8F8;");
        veryEasy.setSelected(true);
        veryEasy.setToggleGroup(group);

        RadioButton easy = new RadioButton("Easy (25 isles)");
        easy.setUserData(Difficulty.EASY);
        easy.setStyle("-fx-text-fill: #F8F8F8;");
        easy.setToggleGroup(group);

        RadioButton medium = new RadioButton("Medium (50 isles)");
        medium.setUserData(Difficulty.MEDIUM);
        medium.setStyle("-fx-text-fill: #F8F8F8;");
        medium.setToggleGroup(group);

        RadioButton hard = new RadioButton("Hard (75 isles)");
        hard.setUserData(Difficulty.HARD);
        hard.setStyle("-fx-text-fill: #F8F8F8;");
        hard.setToggleGroup(group);

        RadioButton challenging = new RadioButton("Challenging (100 isles)");
        challenging.setUserData(Difficulty.CHALLENGING);
        challenging.setStyle("-fx-text-fill: #F8F8F8;");
        challenging.setToggleGroup(group);

        HBox buttons = new HBox(CONTAINER_OFFSET);
        buttons.setPadding(new Insets(20, 0, 0, 20));
        buttons.setPrefWidth(100);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinWidth(buttons.getPrefWidth());
        cancelBtn.setOnAction(e -> controller.switchToBoard());
        Button confirmBtn = new Button("OK");
        confirmBtn.setOnAction(e -> {
            Toggle t = group.getSelectedToggle();
            Difficulty difficulty = (Difficulty) t.getUserData();
            generator.setData(difficulty.getLevel() * 5);
            generator.generateGame();
            controller.setPuzzle();
            controller.switchToBoard();
        });
        confirmBtn.setMinWidth(buttons.getPrefWidth());
        buttons.getChildren().addAll(cancelBtn, confirmBtn);

        vBox.getChildren().addAll(veryEasy, easy, medium, hard, challenging, buttons);
        stackPane.getChildren().addAll(vBox);
        return stackPane;
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
}