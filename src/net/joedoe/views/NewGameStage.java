package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
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
class NewGameStage extends Stage {
    private Generator generator;
    private RadioButton autoBtn;
    private Label heightLabel, widthLabel, islesLabel;
    private TextField heightTxt, widthTxt, islesTxt;
    private CheckBox checkBox;
    private EventHandler<Event> listener;

    /**
     * Erzeugt einen Dialog, in dem der Nutzer Angaben bzgl. Breite und Höhe des
     * Spielfelds sowie Anzahl der zu platzierenden Inseln tätigt, um ein neues
     * Spiel zu erzeugen.
     *
     * @param board Spielfeld, an das die Daten des generierten Spiels zurückgegeben
     *              werden
     */
    NewGameStage() {
        generator = new Generator();
        setTitle("New Puzzle");
        setResizable(false);
        Scene scene = new Scene(setLayout(), 300, 280);
        setScene(scene);
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setStyle("-fx-background-color: #282828;");
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
//        innerPane.setStyle("-fx-background-color: #282828;");
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        innerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        VBox vBox = new VBox(CONTAINER_OFFSET);

        ToggleGroup radios = new ToggleGroup(); //-fx-text-fill: #F8F8F8;
        autoBtn = new RadioButton("Generated size and number of isles");
        autoBtn.setStyle("-fx-text-fill: #F8F8F8;");
        autoBtn.setSelected(true);
        autoBtn.setToggleGroup(radios);
        RadioButton customBtn = new RadioButton("Set size and number of isles yourself");
        customBtn.setStyle("-fx-text-fill: #F8F8F8;");
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
        heightLabel = new Label("Height:");
        heightLabel.setStyle("-fx-text-fill: #F8F8F8;");
        heightLabel.setDisable(true);
        heightTxt = new TextField();
        heightTxt.setDisable(true);

        widthLabel = new Label("Width:");
        widthLabel.setStyle("-fx-text-fill: #F8F8F8;");
        widthTxt = new TextField();
        widthTxt.setDisable(true);
        widthLabel.setDisable(true);

        checkBox = new CheckBox("Set number of isles");
        checkBox.setStyle("-fx-text-fill: #F8F8F8;");
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
        islesLabel = new Label("Isles:");
        islesLabel.setStyle("-fx-text-fill: #F8F8F8;");
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

        HBox buttons = new HBox(CONTAINER_OFFSET);
        buttons.setPadding(new Insets(0, 0, 0, 20));
        buttons.setPrefWidth(100);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinWidth(buttons.getPrefWidth());
        cancelBtn.setOnAction(e -> close());
        Button confirmBtn = new Button("OK");
        confirmBtn.setOnAction(e -> {
            handleInput();
            listener.handle(e);
            close();
        });
        confirmBtn.setMinWidth(buttons.getPrefWidth());
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
                    setAlert("Width and height must be \u2265 " + MIN_WIDTH + " and \u2264 " + MAX_WIDTH + ".");
                    return;
                }
            } else {
                try {
                    int width = Integer.parseInt(widthTxt.getText().trim());
                    int height = Integer.parseInt(heightTxt.getText().trim());
                    int islesCount = Integer.parseInt(islesTxt.getText().trim());
                    generator.setData(width, height, islesCount);
                } catch (IllegalArgumentException e) {
                    setAlert("Width and height must be \u2265 " + MIN_WIDTH + " and \u2264 " + MAX_WIDTH
                            + ".\nNumber of isles must be \u2265 " + MIN_ISLES_COUNT
                            + " and \u2264 Width * Height * 0.2.");
                    return;
                }
            }
        }
        generator.generateGame();
    }

    private void setAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }
}