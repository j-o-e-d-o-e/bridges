package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.joedoe.logics.Generator;

import java.util.Optional;

import static net.joedoe.utils.GameInfo.*;

/**
 * Dialog zur Generierung eines neuen Spiels.
 */
class NewGameFrame extends Stage {
    private Board board;
    private Generator generator;
    private RadioButton autoBtn;
    private Label heightLabel, widthLabel, islesLabel;
    private TextField heightTxt, widthTxt, islesTxt;
    private CheckBox checkBox;

    /**
     * Erzeugt einen Dialog, in dem der Nutzer Angaben bzgl. Breite und Höhe des
     * Spielfelds sowie Anzahl der zu platzierenden Inseln tätigt, um ein neues
     * Spiel zu erzeugen.
     *
     * @param board Spielfeld, an das die Daten des generierten Spiels zurückgegeben
     *              werden
     */
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
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        grid.add(widthLabel, 0, 0);
        grid.add(widthTxt, 1, 0);
        grid.add(heightLabel, 0, 1);
        grid.add(heightTxt, 1, 1);
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

    /**
     * Validiert grundlegend Nutzer-Eingaben und leitet diese an
     * {@link net.joedoe.logics.Generator} weiter.
     */
    private void handleInput() {
        if (autoBtn.isSelected()) generator.setData();
        else {
            if (!checkBox.isSelected()) {
                try {
                    int width = Integer.parseInt(widthTxt.getText().trim());
                    int height = Integer.parseInt(heightTxt.getText().trim());
                    generator.setData(width, height);
                } catch (IllegalArgumentException e) {
                    setAlert("Breite und Höhe müssen \u2265 " + MIN_WIDTH + " und \u2264 " + MAX_WIDTH + " sein.");
                    return;
                }
            } else {
                try {
                    int width = Integer.parseInt(widthTxt.getText().trim());
                    int height = Integer.parseInt(heightTxt.getText().trim());
                    int islesCount = Integer.parseInt(islesTxt.getText().trim());
                    generator.setData(width, height, islesCount);
                } catch (IllegalArgumentException e) {
                    setAlert("Breite und Höhe müssen \u2265 " + MIN_WIDTH + " und \u2264 " + MAX_WIDTH
                            + " sein.\nInselanzahl muss \u2265 " + MIN_ISLES_COUNT
                            + " und \u2264 Breite * Höhe * 0.2 sein.");
                    return;
                }
            }
        }
        generator.generateGame();
        board.setGrid(generator.getWidth(), generator.getHeight(), generator.getIsles(), null);
        // board.setGrid(generator.getWidth(), generator.getHeight(),
        // generator.getIsles(), generator.getBridges());
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