package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class DifficultyFrame extends Stage {
    private ToggleGroup group;
    private EventHandler<DifficultyEvent> listener;

    DifficultyFrame() {
        setTitle("Difficulty");
        setResizable(false);
        Scene scene = new Scene(setLayout(), 250, 280);
        setScene(scene);
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setStyle("-fx-background-color: #282828;");
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        innerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        VBox vBox = new VBox(CONTAINER_OFFSET);
        group = new ToggleGroup();

        RadioButton veryEasy = new RadioButton("Very easy (25 isles)");
        veryEasy.setUserData(Difficulty.VERY_EASY);
        veryEasy.setStyle("-fx-text-fill: #F8F8F8;");
        veryEasy.setSelected(true);
        veryEasy.setToggleGroup(group);

        RadioButton easy = new RadioButton("Easy (50 isles)");
        easy.setUserData(Difficulty.EASY);
        easy.setStyle("-fx-text-fill: #F8F8F8;");
        easy.setToggleGroup(group);

        RadioButton medium = new RadioButton("Medium (75 isles)");
        medium.setUserData(Difficulty.MEDIUM);
        medium.setStyle("-fx-text-fill: #F8F8F8;");
        medium.setToggleGroup(group);

        RadioButton harder = new RadioButton("Hard (100 isles)");
        harder.setUserData(Difficulty.HARD);
        harder.setStyle("-fx-text-fill: #F8F8F8;");
        harder.setToggleGroup(group);

        RadioButton hard = new RadioButton("Challenging (125 isles)");
        hard.setUserData(Difficulty.CHALLENGING);
        hard.setStyle("-fx-text-fill: #F8F8F8;");
        hard.setToggleGroup(group);

        HBox buttons = new HBox(CONTAINER_OFFSET);
        buttons.setPadding(new Insets(20, 0, 0, 20));
        buttons.setPrefWidth(100);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinWidth(buttons.getPrefWidth());
        cancelBtn.setOnAction(e -> close());
        Button confirmBtn = new Button("OK");
        confirmBtn.setOnAction(e -> {
            Toggle t = group.getSelectedToggle();
            Difficulty difficulty = (Difficulty) t.getUserData();
            listener.handle(new DifficultyEvent(difficulty));
            close();
        });
        confirmBtn.setMinWidth(buttons.getPrefWidth());
        buttons.getChildren().addAll(cancelBtn, confirmBtn);

        vBox.getChildren().addAll(veryEasy, easy, medium, harder, hard, buttons);
        innerPane.getChildren().addAll(vBox);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    void setListener(EventHandler<DifficultyEvent> listener) {
        this.listener = listener;
    }

    public class DifficultyEvent extends Event {
        private Difficulty difficulty;

        DifficultyEvent(Difficulty difficulty) {
            super(null);
            this.difficulty = difficulty;
        }

        Difficulty getDifficulty() {
            return difficulty;
        }
    }

    public enum Difficulty {
        VERY_EASY(5), EASY(10), MEDIUM(15), HARD(20), CHALLENGING(25);

        private int level;

        Difficulty(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
}