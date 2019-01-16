package net.joedoe.views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.joedoe.utils.GameManager.Mode;

class ModeChooser extends StackPane {
    private final SceneController controller;

    ModeChooser(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        getChildren().add(setLayout());
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        VBox box = new VBox();
        Button level = new Button("Level");
        level.setOnAction(e -> controller.createNewGame(Mode.LEVEL));
        Button time = new Button("Time");
        time.setOnAction(e -> controller.createNewGame(Mode.TIME));
        Button free = new Button("Free");
        free.setOnAction(e -> controller.createNewGame(Mode.FREE));
        box.getChildren().addAll(level, time, free);
        return box;
    }
}
