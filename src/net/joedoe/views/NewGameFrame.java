package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.joedoe.controllers.GameGenerator;

import java.util.Optional;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class NewGameFrame extends Stage {
    private Board board;
    private GameGenerator controller;
    private RadioButton autoBtn;
    private Label heightLabel, widthLabel, islesLabel;
    private TextField heightTxt, widthTxt, islesTxt;
    private CheckBox checkBox;

    NewGameFrame(Board board) {
        this.board = board;
        controller = new GameGenerator();
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
                heightLabel.setDisable(true);
                heightTxt.setDisable(true);
                widthLabel.setDisable(true);
                widthTxt.setDisable(true);
                checkBox.setDisable(true);
                checkBox.setSelected(false);
                islesLabel.setDisable(true);
                islesTxt.setDisable(true);
            } else {
                heightLabel.setDisable(false);
                heightTxt.setDisable(false);
                widthLabel.setDisable(false);
                widthTxt.setDisable(false);
                checkBox.setDisable(false);

            }
        });
        heightLabel = new Label("Höhe:");
        heightLabel.setDisable(true);
        heightTxt = new TextField();
        heightTxt.setDisable(true);

        widthLabel = new Label("Breite:");
        widthTxt = new TextField();
        widthTxt.setDisable(true);
        widthLabel.setDisable(true);

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
        grid.setPadding(new Insets(0, 0, 0, 50));
        grid.getColumnConstraints().add(new ColumnConstraints(50));
        grid.setVgap(10);
        grid.add(heightLabel, 0, 0);
        grid.add(heightTxt, 1, 0);
        grid.add(widthLabel, 0, 1);
        grid.add(widthTxt, 1, 1);
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
        int width, height, isleCount;
        if (autoBtn.isSelected()) {
            controller.setHeight();
            controller.setWidth();
            controller.setIsleCount();
//            controller.setHeight(25);
//            controller.setWidth(25);
//            controller.setIsleCount((int) (0.2 * controller.getHeight() * controller.getWidth()));
            controller.generateGame();
            board.setGrid(controller.getHeight(), controller.getWidth(), controller.getFinalIsles(), controller.getFinalBridges());
        } else {
            width = Integer.parseInt(widthTxt.getText());
            height = Integer.parseInt(heightTxt.getText());
            if (width < 4 || width > 25 || height < 4 || height > 25) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ungültige Eingabe");
                alert.setHeaderText("Breite und Höhe müssen größer 4 und kleiner 25 sein.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK)
                    alert.close();
                reset();
                return;
            }
            if (!checkBox.isSelected()) {
                controller.setHeight(height);
                controller.setWidth(width);
                controller.setIsleCount();
                controller.generateGame();
                board.setGrid(height, width, controller.getFinalIsles(), controller.getFinalBridges());
            } else {
                isleCount = Integer.parseInt(islesTxt.getText());
                if (isleCount < 2 || isleCount > 0.2 * width * height) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Ungültige Eingabe");
                    alert.setHeaderText("Inselanzahl muss größer 2 und kleiner Breite * Höhe * 0.2 sein.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK)
                        alert.close();
                    reset();
                    return;
                }
                controller.setHeight(height);
                controller.setWidth(width);
                controller.setIsleCount(isleCount);
                controller.generateGame();
                board.setGrid(height, width, controller.getFinalIsles(), controller.getFinalBridges());
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