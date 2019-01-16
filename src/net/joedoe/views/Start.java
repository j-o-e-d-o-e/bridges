package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class Start extends StackPane {
    private final SceneController controller;

    Start(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        getChildren().add(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        VBox box = new VBox(CONTAINER_OFFSET);
        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button highScore = new Button("Highscore");
        highScore.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button tutorial = new Button("Tutorial");
        tutorial.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button exit = new Button("Quit");
        exit.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        box.getChildren().addAll(newGame, highScore, tutorial, exit);
        return box;
    }
}
