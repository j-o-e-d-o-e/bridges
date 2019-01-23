package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.joedoe.utils.FileHandler;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class Start extends BorderPane {
    private final SceneController controller;
    private Button loadGame;

    Start(SceneController controller) {
        this.controller = controller;
        setStyle("-fx-background-color: #282828;");
        setTop(new TopBar(controller, "Hashiwokakero"));
        setCenter(setLayout());
        if (FileHandler.fileExists()) {
            loadGame.setDisable(true);
        }
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

        Label title = new Label("Hashiwokakero");
        title.setFont(Font.font(40));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));
        title.setStyle("-fx-text-fill: ghostwhite");

        Button newGame = new Button("New Game");
        newGame.setPrefWidth(100);
        newGame.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        loadGame = new Button("Load Game");
        loadGame.setPrefWidth(100);
        loadGame.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button highScore = new Button("Highscore");
        highScore.setPrefWidth(100);
        highScore.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button tutorial = new Button("Rules");
        tutorial.setPrefWidth(100);
        tutorial.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        Button exit = new Button("Quit");
        exit.setPrefWidth(100);
        exit.setOnAction(e -> {
            String text = ((Button) e.getSource()).getText();
            controller.goTo(text);
        });
        box.getChildren().addAll(title, newGame, loadGame, highScore, tutorial, exit);
        innerPane.getChildren().add(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }
}
