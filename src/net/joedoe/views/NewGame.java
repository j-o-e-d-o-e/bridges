package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.joedoe.controllers.NewGameController;
import net.joedoe.entities.Isle;

import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.utils.GameInfo.ONE_TILE;

class NewGame extends Stage {
    private Board board;
    private NewGameController controller;
    private RadioButton autoBtn;
    private Label widthLabel, heightLabel, islesLabel;
    private TextField widthTxt, heightTxt, islesTxt;
    private CheckBox checkBox;

    NewGame(Board board) {
        this.board = board;
        controller = new NewGameController();
        setTitle("Neues Rätsel");
        Scene scene = new Scene(setLayout(), 400, 350);
        setScene(scene);
    }

    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        innerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        VBox vBox = new VBox();
        vBox.setSpacing(CONTAINER_OFFSET);

        ToggleGroup radios = new ToggleGroup();
        autoBtn = new RadioButton("Automatische Größe und Inselanzahl");
        autoBtn.setSelected(true);
        autoBtn.setToggleGroup(radios);
        RadioButton customBtn = new RadioButton("Größe und/oder Inselanzahl selbst festlegen");
        customBtn.setToggleGroup(radios);
        radios.selectedToggleProperty().addListener(e -> {
            if (autoBtn.isSelected()) {
                widthLabel.setDisable(true);
                widthTxt.setDisable(true);
                heightLabel.setDisable(true);
                heightTxt.setDisable(true);
                checkBox.setDisable(true);
                checkBox.setSelected(false);
                islesLabel.setDisable(true);
                islesTxt.setDisable(true);
            } else {
                widthLabel.setDisable(false);
                widthTxt.setDisable(false);
                heightLabel.setDisable(false);
                heightTxt.setDisable(false);
                checkBox.setDisable(false);

            }
        });
        widthLabel = new Label("Breite:");
        widthTxt = new TextField();
        widthTxt.setDisable(true);
        widthLabel.setDisable(true);
        heightLabel = new Label("Höhe:");
        heightLabel.setDisable(true);
        heightTxt = new TextField();
        heightTxt.setDisable(true);
        checkBox = new CheckBox("Inselanzahl festlegen");
        checkBox.setDisable(true);
        checkBox.setOnAction(e -> {
            if (islesTxt.isDisabled()) {
                islesLabel.setDisable(false);
                islesTxt.setDisable(false);
            } else {
                islesLabel.setDisable(true);
                islesTxt.setDisable(true);
            }
        });
        islesLabel = new Label("Inseln:");
        islesLabel.setDisable(true);
        islesTxt = new TextField();
        islesTxt.setDisable(true);

        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(0, 0, 0, CONTAINER_OFFSET));
        grid.getColumnConstraints().add(new ColumnConstraints(ONE_TILE));
        grid.setVgap(10);
        grid.add(widthLabel, 0, 0);
        grid.add(widthTxt, 1, 0);
        grid.add(heightLabel, 0, 1);
        grid.add(heightTxt, 1, 1);
        grid.add(checkBox, 0, 2);
        GridPane.setColumnSpan(checkBox, 3);
        grid.add(islesLabel, 0, 3);
        grid.add(islesTxt, 1, 3);

        HBox buttons = new HBox();
        buttons.setPadding(new Insets(0, 0, 0, CONTAINER_OFFSET * 3));
        buttons.setPrefWidth(100);
        Button cancelBtn = new Button("Abbrechen");
        cancelBtn.setMinWidth(buttons.getPrefWidth());
        cancelBtn.setOnAction(e -> close());
        Button confirmBtn = new Button("OK");
        confirmBtn.setOnAction(e -> handleInput());
        confirmBtn.setMinWidth(buttons.getPrefWidth());
        buttons.setSpacing(CONTAINER_OFFSET);
        buttons.getChildren().addAll(cancelBtn, confirmBtn);

        vBox.getChildren().addAll(autoBtn, customBtn, grid, buttons);
        innerPane.getChildren().addAll(vBox);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    private void handleInput() {
        List<Isle> generatedIsles;
        int width, height, isles;
        if (autoBtn.isSelected()) {
            generatedIsles = controller.createGame();
            board.setGrid(controller.getHeight(), controller.getWidth(), generatedIsles);
        } else {
            try {
                width = Integer.parseInt(widthTxt.getText());
                height = Integer.parseInt(heightTxt.getText());
                if (width < 4 || width > 25 || height < 4 || height > 25) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                reset();
                return;
            }
            if (!checkBox.isSelected()) {
                generatedIsles = controller.createGame(height, width);
                board.setGrid(height, width, generatedIsles);
            } else {
                try {
                    isles = Integer.parseInt(islesTxt.getText());
                    if (isles < 2 || isles > 0.2 * width * height) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    reset();
                    return;
                }
                generatedIsles = controller.createGame(height, width, isles);
                board.setGrid(height, width, generatedIsles);
            }
        }
        close();
    }

    private void reset() {
        widthTxt.setText("");
        heightTxt.setText("");
        islesTxt.setText("");
    }
}