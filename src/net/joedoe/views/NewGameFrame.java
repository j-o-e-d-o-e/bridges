package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.joedoe.logics.Generator;

import java.util.Optional;

import static net.joedoe.utils.GameInfo.*;

class NewGameFrame extends Stage {
    private Board board;
    private Generator generator;
    private RadioButton autoBtn;
    private Label heightLabel, widthLabel, islesLabel;
    private TextField heightTxt, widthTxt, islesTxt;
    private CheckBox checkBox;

    NewGameFrame(Board board) {
        this.board = board;
        generator = new Generator();
        setTitle("Neues Rätsel");
        setResizable(false);
        Scene scene = new Scene(setLayout(), 300, 280);
        setScene(scene);
    }

    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(STD_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        // grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(0, 0, 0, 30));
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
        buttons.setPadding(new Insets(0, 0, 0, 20));
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
        if (autoBtn.isSelected()) {
            generator.setHeight();
            generator.setWidth();
            generator.setIslesCount();
            // generator.setHeight(MAX_HEIGHT);
            // generator.setWidth(MAX_WIDTH);
            // generator.setIslesCount(MAX_ISLES_COUNT);
        } else {
            String width = widthTxt.getText();
            if (width.isEmpty()) {
                generator.setWidth();
            } else {
                try {
                    generator.setWidth(Integer.parseInt(width));
                } catch (IllegalArgumentException e) {
                    setAlert("Breite und Höhe müssen größer/gleich " + MIN_WIDTH + " und kleiner/gleich " + MAX_WIDTH
                            + " sein.");
                    return;
                }
            }
            String height = heightTxt.getText();
            if (height.isEmpty()) {
                generator.setHeight();
            } else {
                try {
                    generator.setHeight(Integer.parseInt(height));
                } catch (IllegalArgumentException e) {
                    setAlert("Breite und Höhe müssen größer/gleich " + MIN_HEIGHT + " und kleiner/gleich " + MAX_HEIGHT
                            + " sein.");
                    return;
                }
            }
            String isles = islesTxt.getText();
            if (!checkBox.isSelected() || isles.isEmpty()) {
                generator.setIslesCount();
            } else {
                try {
                    generator.setIslesCount(Integer.parseInt(isles));
                } catch (IllegalArgumentException e) {
                    setAlert("Inselanzahl muss größer/gleich " + MIN_ISLES_COUNT
                            + " und kleiner/gleich Höhe * Breite * 0.2 sein.");
                    return;
                }
            }
        }
        generator.generateGame();
        board.setGrid(generator.getWidth(), generator.getHeight(), generator.getFinalIsles(), null);
        // board.setGrid(generator.getWidth(), generator.getHeight(),
        // generator.getFinalIsles(), generator.getFinalBridges());
        close();
    }

    private void setAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ungültige Eingabe");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }
}